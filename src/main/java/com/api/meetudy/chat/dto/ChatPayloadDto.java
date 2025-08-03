package com.api.meetudy.chat.dto;

import com.api.meetudy.chat.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatPayloadDto implements Serializable {

    private Long roomId;

    private Long senderId;

    private String message;

    private MessageType messageType;

}