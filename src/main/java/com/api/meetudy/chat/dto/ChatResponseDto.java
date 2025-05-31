package com.api.meetudy.chat.dto;

import com.api.meetudy.chat.entity.Chat;
import com.api.meetudy.chat.enums.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatResponseDto {

    @Schema(description = "The unique identifier of the chat room.",
            example = "1")
    private Long roomId;

    @Schema(description = "The unique identifier of the sender.",
            example = "1")
    private Long senderId;

    @Schema(description = "The nickname of the sender.",
            example = "username")
    private String senderName;

    @Schema(description = "The content of the chat message.",
            example = "Hello!")
    private String message;

    @Schema(description = "The type of the message.",
            example = "ENTER, TALK, QUIT")
    private MessageType messageType;

    @Schema(description = "The timestamp when the message was created.",
            example = "2025-05-31T14:22:00")
    private LocalDateTime createdAt;

    @Schema(description = "Indicates whether the message was sent by the current user.",
            example = "true")
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