package com.api.meetudy.chat.service;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.chat.dto.ChatMessageDto;
import com.api.meetudy.chat.dto.ChatResponseDto;
import com.api.meetudy.chat.dto.ChatRoomInfoDto;
import com.api.meetudy.chat.entity.Chat;
import com.api.meetudy.chat.entity.ChatRoom;
import com.api.meetudy.chat.entity.ChatRoomMember;
import com.api.meetudy.chat.repository.ChatRepository;
import com.api.meetudy.chat.repository.ChatRoomMemberRepository;
import com.api.meetudy.chat.repository.ChatRoomRepository;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.global.utils.LeaderAccessValidator;
import com.api.meetudy.group.enums.GroupMemberStatus;
import com.api.meetudy.group.repository.GroupMemberRepository;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.group.entity.StudyGroup;
import com.api.meetudy.group.entity.StudyGroupMember;
import com.api.meetudy.group.repository.GroupRepository;
import com.api.meetudy.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final LeaderAccessValidator leaderAccessValidator;
    private final AuthenticationService authenticationService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public String createPrivateRoom(Long groupId, Member sender) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        Member receiver = studyGroup.getLeader().getMember();
        chatRoomRepository.save(ChatRoom.createPrivateRoom(sender, receiver));

        return "Chat room has been successfully created.";
    }

    @Transactional
    public String createGroupRoom(Long groupId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));
        leaderAccessValidator.checkLeaderAccess(member, studyGroup);

        Set<Member> members = studyGroup.getMembers().stream()
                .filter(m -> m.getStatus() == GroupMemberStatus.LEADER || m.getStatus() == GroupMemberStatus.MEMBER)
                .map(StudyGroupMember::getMember)
                .collect(Collectors.toSet());

        if (members.size() < 2) {
            throw new CustomException(ErrorStatus.INSUFFICIENT_CHAT_MEMBERS);
        }

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.createGroupRoom(members, studyGroup.getName()));
        chatRoom.updateStudyGroup(studyGroup);

        return "Chat room has been successfully created.";
    }

    @Transactional(readOnly = true)
    public List<ChatRoomInfoDto> getChatRoomInfo(Member member) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMembers_Member(member);

        return chatRooms.stream().map(room -> {
            String displayName = room.getMembers().stream()
                    .filter(m -> m.getMember().equals(member))
                    .findFirst()
                    .map(ChatRoomMember::getDisplayName)
                    .orElse("Unknown");

            List<String> memberNicknames = room.getMembers().stream()
                    .map(m -> m.getMember().getNickname())
                    .collect(Collectors.toList());

            Chat lastChat = chatRepository.findTopByRoomOrderByCreatedAtDesc(room).orElse(null);

            return ChatRoomInfoDto.builder()
                    .roomId(room.getId())
                    .groupName(room.getGroupName())
                    .memberNicknames(memberNicknames)
                    .displayName(displayName)
                    .lastMessage(lastChat != null ? lastChat.getMessage() : null)
                    .lastMessageTime(lastChat != null ? lastChat.getCreatedAt() : null)
                    .build();
        }).collect(Collectors.toList());
    }

    public void sendChatAndNotify(ChatMessageDto messageDto, String username) {
        Member sender = authenticationService.getCurrentMemberByUsername(username);

        ChatRoom room = chatRoomRepository.findByIdWithMembers(messageDto.getRoomId())
                .orElseThrow(() -> new CustomException(ErrorStatus.CHAT_ROOM_NOT_FOUND));

        Chat chat = saveMessage(messageDto, sender, room);

        List<Member> otherMembers = findOtherMembersInRoom(room, sender);
        for (Member receiver : otherMembers) {
            notificationService.sendChatNotification(receiver, chat);
        }

        simpMessagingTemplate.convertAndSend(
                "/sub/chat/room/" + messageDto.getRoomId(),
                messageDto
        );
    }

    @Transactional
    public Chat saveMessage(ChatMessageDto messageDto, Member sender, ChatRoom room) {
        Chat chat = Chat.createChat(sender, messageDto.getMessage(), messageDto.getMessageType(), room);
        return chatRepository.save(chat);
    }

    @Transactional(readOnly = true)
    public List<ChatResponseDto> findChatsByRoomId(Long roomId, Member sender) {
        List<Chat> chats = chatRepository.findByRoom_IdOrderByCreatedAtAsc(roomId);
        return chats.stream()
                .map(chat -> new ChatResponseDto(chat, sender.getId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long getChatRoomByStudyGroupId(Long groupId) {
        ChatRoom chatRoom = chatRoomRepository.findByStudyGroup_Id(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.CHAT_ROOM_NOT_FOUND));

        return chatRoom.getId();
    }

    @Transactional
    public String leaveChatRoom(Long roomId, Member member) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorStatus.CHAT_ROOM_NOT_FOUND));

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, member)
                .orElseThrow(() -> new CustomException(ErrorStatus.CHAT_ROOM_MEMBER_NOT_FOUND));

        chatRoom.getMembers().remove(chatRoomMember);
        chatRoomMemberRepository.delete(chatRoomMember);

        if (chatRoom.getMembers().isEmpty()) {
            chatRoomRepository.delete(chatRoom);
        }

        return "You have left the chat room.";
    }

    @Transactional(readOnly = true)
    public List<Member> findOtherMembersInRoom(ChatRoom room, Member sender) {
        return room.getMembers().stream()
                .map(ChatRoomMember::getMember)
                .filter(member -> !member.getId().equals(sender.getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Long> getStudyGroupIdsWithChatRoom(Member member) {
        return groupMemberRepository.findByMemberAndStatus(member, GroupMemberStatus.LEADER).stream()
                .map(StudyGroupMember::getStudyGroup)
                .filter(studyGroup -> studyGroup.getChatRoom() != null)
                .map(StudyGroup::getId)
                .toList();
    }

}