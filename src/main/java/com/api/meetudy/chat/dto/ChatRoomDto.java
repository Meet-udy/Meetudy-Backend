package com.api.meetudy.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomDto {

    private Long roomId;

    private String displayName;

}