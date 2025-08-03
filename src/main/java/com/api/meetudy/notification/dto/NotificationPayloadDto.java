package com.api.meetudy.notification.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPayloadDto {

    private Long receiverId;

    private String message;

    private Long postId;

    private Long chatId;

}