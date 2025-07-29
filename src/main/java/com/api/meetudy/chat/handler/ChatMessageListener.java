package com.api.meetudy.chat.handler;

import com.api.meetudy.chat.dto.ChatMessageDto;
import com.api.meetudy.chat.dto.ChatPayload;
import com.api.meetudy.chat.entity.Chat;
import com.api.meetudy.chat.entity.ChatRoom;
import com.api.meetudy.chat.entity.ChatRoomMember;
import com.api.meetudy.chat.repository.ChatRepository;
import com.api.meetudy.chat.repository.ChatRoomRepository;
import com.api.meetudy.global.config.RabbitMqConfig;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.member.repository.MemberRepository;
import com.api.meetudy.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessageListener {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMqConfig.CHAT_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void handleChatMessage(ChatPayload payload) {
        ChatRoom room = chatRoomRepository.findByIdWithMembers(payload.getRoomId())
                .orElseThrow(() -> new CustomException(ErrorStatus.CHAT_ROOM_NOT_FOUND));

        Member sender = memberRepository.findById(payload.getSenderId()).get();

        Chat chat = Chat.createChat(sender, payload.getMessage(), payload.getMessageType(), room);
        chatRepository.save(chat);

        List<Member> otherMembers = room.getMembers().stream()
                .map(ChatRoomMember::getMember)
                .filter(m -> !m.getId().equals(sender.getId()))
                .toList();

        for (Member receiver : otherMembers) {
            notificationService.sendChatNotification(receiver, chat);
        }

        ChatMessageDto responseDto = new ChatMessageDto();
        responseDto.setRoomId(payload.getRoomId());
        responseDto.setSenderId(sender.getId());
        responseDto.setMessage(payload.getMessage());
        responseDto.setMessageType(payload.getMessageType());

        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + payload.getRoomId(), responseDto);
    }

}