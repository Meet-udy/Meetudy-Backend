package com.api.meetudy.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ChatRoomInfoDto {

    @Schema(description = "The unique identifier of the chat room.",
            example = "1")
    private Long roomId;

    @Schema(description = "The group name of the chat room, null if the room is private.",
            example = "Study Group")
    private String groupName;

    @Schema(description = "The display name of the chat room, which can be a group name or participant name.",
            example = "Study Group")
    private String displayName;

    @Schema(description = "The last message sent in the chat room.",
            example = "See you tomorrow.")
    private String lastMessage;

    @Schema(description = "The timestamp of the last message sent in the chat room.",
            example = "2025-05-31T14:22:00")
    private LocalDateTime lastMessageTime;

    @Schema(description = "List of nicknames of members in the chat room.",
            example = "[\"user1\", \"user2\"]")
    private List<String> memberNicknames;

}