package com.api.meetudy.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ChatRoomInfoDto {

    private Long roomId;

    // isPrivate == true면 null
    private String groupName;

    private String displayName;

    private String lastMessage;

    private LocalDateTime lastMessageTime;

    private List<String> memberNicknames;

}