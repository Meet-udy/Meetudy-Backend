package com.api.meetudy.notification.service;

import com.api.meetudy.auth.provider.JwtTokenProvider;
import com.api.meetudy.chat.entity.Chat;
import com.api.meetudy.chat.repository.ChatRepository;
import com.api.meetudy.comment.entity.Comment;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.member.repository.MemberRepository;
import com.api.meetudy.notification.dto.NotificationDto;
import com.api.meetudy.notification.entity.Notification;
import com.api.meetudy.notification.mapper.NotificationMapper;
import com.api.meetudy.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new CustomException(ErrorStatus.TOKEN_INVALID);
        }

        String username = jwtTokenProvider.getUsernameFromToken(token);

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND));
        Long memberId = member.getId();

        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);
        emitters.put(memberId, emitter);

        emitter.onTimeout(() -> emitters.remove(memberId));
        emitter.onCompletion(() -> emitters.remove(memberId));

        return emitter;
    }

    public void sendCommentNotification(Member receiver, Long postId, Comment comment) {
        if (receiver == null) return;

        Notification notification = Notification.builder()
                .receiver(receiver)
                .message(comment.getContent())
                .postId(postId)
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        SseEmitter emitter = emitters.get(receiver.getId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("comment")
                        .data(NotificationDto.from(notification)));
            } catch (IOException e) {
                emitters.remove(receiver.getId());
            }
        }
    }

    public void sendChatNotification(Member receiver, Chat chat) {
        if (receiver == null || chat == null) return;

        Notification notification = Notification.builder()
                .receiver(receiver)
                .message(chat.getMessage())
                .chatId(chat.getId())
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        Long chatRoomId = chat.getRoom() != null ? chat.getRoom().getId() : null;

        SseEmitter emitter = emitters.get(receiver.getId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("chat")
                        .data(NotificationDto.from(notification, chatRoomId)));
            } catch (IOException e) {
                emitters.remove(receiver.getId());
            }
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> getAllNotifications(Member member) {
        List<Notification> notifications = notificationRepository.findByReceiverOrderByCreatedAtDesc(member);

        return notifications.stream()
                .map(notification -> {
                    Long chatRoomId = null;
                    if (notification.getChatId() != null) {
                        chatRoomId = chatRepository.findById(notification.getChatId())
                                .map(chat -> chat.getRoom().getId())
                                .orElse(null);
                    }
                    return NotificationDto.from(notification, chatRoomId);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public String markAsRead(Long notificationId, Member member) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOTIFICATION_NOT_FOUND));

        if (!notification.getReceiver().getId().equals(member.getId())) {
            throw new CustomException(ErrorStatus.NOTIFICATION_ACCESS_DENIED);
        }

        notification.markAsRead();
        notificationRepository.save(notification);

        return "Notification marked as read.";
    }

}