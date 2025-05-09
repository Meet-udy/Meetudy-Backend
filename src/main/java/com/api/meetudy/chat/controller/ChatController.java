package com.api.meetudy.chat.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.chat.dto.*;
import com.api.meetudy.chat.entity.ChatRoom;
import com.api.meetudy.chat.service.ChatService;
import com.api.meetudy.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;
    private final AuthenticationService authenticationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Operation(summary = "개인 채팅방 생성 API")
    @PostMapping("/room/private")
    public ResponseEntity<ApiResponse<ChatRoom>> createPrivateRoom(@RequestBody ChatRoomRequestDto dto) {
        ChatRoom response = chatService.createPrivateRoom(dto.getSender(), dto.getReceiver());
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "그룹 채팅방 생성 API")
    @PostMapping("/room/group/{groupId}")
    public ResponseEntity<ApiResponse<ChatRoomDto>> createGroupRoom(@PathVariable Long groupId, Principal principal) {
        ChatRoomDto response = chatService.createGroupRoom(groupId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "채팅방 정보 조회 API")
    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatRoomInfoDto>>> getChatRoomInfo(Principal principal) {
        List<ChatRoomInfoDto> response = chatService.getChatRoomInfo(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "그룹 채팅방 메시지 전송 API")
    @MessageMapping("/message")
    public void sendMessage(ChatMessageDto messageDto, StompHeaderAccessor headerAccessor) throws Exception {
        Principal principal = headerAccessor.getUser();
        if (principal != null) {
            String username = principal.getName();
            chatService.saveMessage(messageDto, authenticationService.getCurrentMemberByUsername(username));
        }

        simpMessagingTemplate.convertAndSend(
                "/sub/chat/room/" + messageDto.getRoomId(),
                messageDto
        );
    }

    @Operation(summary = "특정 채팅방 메시지 조회 API")
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatResponseDto>>> getMessages(@PathVariable Long roomId, Principal principal) {
        List<ChatResponseDto> response = chatService.findChatsByRoomId(roomId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "채팅방 나가기 API")
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<String>> leaveChatRoom(@PathVariable Long roomId,
                                                             Principal principal) {
        String response = chatService.leaveChatRoom(roomId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}