package com.api.meetudy.chat.service;

import com.api.meetudy.chat.dto.ChatMessageDto;
import com.api.meetudy.chat.entity.Chat;
import com.api.meetudy.chat.entity.ChatRoom;
import com.api.meetudy.chat.repository.ChatRepository;
import com.api.meetudy.chat.repository.ChatRoomRepository;
import com.api.meetudy.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createPrivateRoom(Member sender, Member receiver) {
        return chatRoomRepository.save(ChatRoom.createPrivateRoom(sender, receiver));
    }

    public ChatRoom createGroupRoom(Set<Member> members) {
        return chatRoomRepository.save(ChatRoom.createGroupRoom(members));
    }

    public Chat saveMessage(ChatMessageDto messageDto) {
        ChatRoom room = chatRoomRepository.findById(messageDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        Chat chat = Chat.createChat(messageDto.getSender(), messageDto.getMessage(), room);
        return chatRepository.save(chat);
    }

}
