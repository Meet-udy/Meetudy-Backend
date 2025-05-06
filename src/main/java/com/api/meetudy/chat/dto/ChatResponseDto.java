package com.api.meetudy.chat.dto;

import com.api.meetudy.chat.entity.Chat;
import com.api.meetudy.chat.enums.MessageType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatResponseDto {

    private Long roomId;

    private Long senderId;

    private String senderName;

    private String message;

    private MessageType messageType;

    private LocalDateTime createdAt;

    private boolean mine;

    public ChatResponseDto(Chat chat, Long memberId) {
        this.roomId = chat.getRoom().getId();
        this.senderId = chat.getSender().getId();
        this.senderName = chat.getSender().getNickname();
        this.message = chat.getMessage();
        this.messageType = chat.getMessageType();
        this.createdAt = chat.getCreatedAt();
        this.mine = chat.getSender().getId().equals(memberId);
    }

}