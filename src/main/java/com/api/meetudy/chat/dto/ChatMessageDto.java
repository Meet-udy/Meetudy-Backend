package com.api.meetudy.chat.dto;

import com.api.meetudy.chat.enums.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {

    @Schema(description = "The unique identifier of the chat room.",
            example = "1")
    private Long roomId;

    @Schema(description = "The unique identifier of the sender.",
            example = "1")
    private Long senderId;

    @Schema(description = "The content of the chat message.",
            example = "Hello!")
    private String message;

    @Schema(description = "The type of the message.",
            example = "ENTER, TALK, QUIT")
    private MessageType messageType;

}