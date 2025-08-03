package com.api.meetudy.notification.consumer;

import com.api.meetudy.chat.repository.ChatRepository;
import com.api.meetudy.global.config.RabbitMqConfig;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.member.repository.MemberRepository;
import com.api.meetudy.notification.dto.NotificationPayloadDto;
import com.api.meetudy.notification.entity.Notification;
import com.api.meetudy.notification.repository.NotificationRepository;
import com.api.meetudy.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMqConfig.NOTIFICATION_QUEUE)
    public void handleNotification(NotificationPayloadDto payload) {
        Member receiver = memberRepository.findById(payload.getReceiverId())
                .orElse(null);
        if (receiver == null) return;

        Notification notification = Notification.builder()
                .receiver(receiver)
                .message(payload.getMessage())
                .postId(payload.getPostId())
                .chatId(payload.getChatId())
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        Long chatRoomId = null;
        if (payload.getChatId() != null) {
            chatRoomId = chatRepository.findById(payload.getChatId())
                    .map(chat -> chat.getRoom().getId())
                    .orElse(null);
        }

        notificationService.sendSseNotification(receiver, notification, chatRoomId);
    }

}