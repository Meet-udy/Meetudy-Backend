package com.api.meetudy.chat.dto;

import com.api.meetudy.chat.enums.MessageType;
import lombok.Getter;

@Getter
public class ChatMessageDto {

    private Long roomId;

    private String sender;

    private String message;

    private MessageType messageType;

}
