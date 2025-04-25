package com.api.meetudy.chat.controller;

import com.api.meetudy.chat.dto.ChatMessageDto;
import com.api.meetudy.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/message")
    @SendTo("/sub/chat")
    public ChatMessageDto sendMessage(ChatMessageDto messageDto) throws Exception {
        chatService.saveMessage(messageDto);
        return messageDto;
    }

    @MessageMapping("/private")
    @SendToUser("/queue/private")
    public ChatMessageDto sendPrivateMessage(ChatMessageDto messageDto) throws Exception {
        chatService.saveMessage(messageDto);
        return messageDto;
    }

}
