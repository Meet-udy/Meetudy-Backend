package com.api.meetudy.notification.dto;

import com.api.meetudy.notification.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationDto {

    @Schema(description = "The unique identifier of the notification.",
            example = "1")
    private Long notificationId;

    @Schema(description = "The message content of the notification.",
            example = "A new comment has been added to your post.")
    private String message;

    @Schema(description = "The ID of the post related to the notification.",
            example = "42")
    private Long postId;

    @Schema(description = "Indicates whether the notification has been read.",
            example = "false")
    private boolean isRead;

    @Schema(description = "The time when the notification was created.",
            example = "2025-05-31T14:22:00")
    private LocalDateTime createdAt;

    public static NotificationDto from(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getMessage(),
                notification.getPostId(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }

}