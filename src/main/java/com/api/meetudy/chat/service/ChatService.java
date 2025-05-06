package com.api.meetudy.chat.service;

import com.api.meetudy.chat.dto.ChatMessageDto;
import com.api.meetudy.chat.dto.ChatResponseDto;
import com.api.meetudy.chat.dto.ChatRoomDto;
import com.api.meetudy.chat.dto.ChatRoomInfoDto;
import com.api.meetudy.chat.entity.Chat;
import com.api.meetudy.chat.entity.ChatRoom;
import com.api.meetudy.chat.entity.ChatRoomMember;
import com.api.meetudy.chat.repository.ChatRepository;
import com.api.meetudy.chat.repository.ChatRoomRepository;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.global.utils.LeaderAccessValidator;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
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
    private final GroupRepository groupRepository;
    private final LeaderAccessValidator leaderAccessValidator;

    @Transactional
    public ChatRoom createPrivateRoom(Member sender, Member receiver) {
        return chatRoomRepository.save(ChatRoom.createPrivateRoom(sender, receiver));
    }

    @Transactional
    public ChatRoomDto createGroupRoom(Long groupId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));
        leaderAccessValidator.checkLeaderAccess(member, studyGroup);

        Set<Member> members = studyGroup.getMembers().stream()
                .map(StudyGroupMember::getMember)
                .collect(Collectors.toSet());

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.createGroupRoom(members));

        return ChatRoomDto.builder()
                .roomId(chatRoom.getId())
                .displayName(chatRoom.getGroupName())
                .build();
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

    @Transactional
    public Chat saveMessage(ChatMessageDto messageDto, Member sender) {
        ChatRoom room = chatRoomRepository.findById(messageDto.getRoomId())
                .orElseThrow(() -> new CustomException(ErrorStatus.CHAT_ROOM_NOT_FOUND));

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

}