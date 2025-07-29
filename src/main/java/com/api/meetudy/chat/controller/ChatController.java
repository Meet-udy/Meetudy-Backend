package com.api.meetudy.chat.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.chat.dto.*;
import com.api.meetudy.chat.entity.ChatRoom;
import com.api.meetudy.chat.repository.ChatRoomRepository;
import com.api.meetudy.chat.service.ChatService;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetudy/chats")
public class ChatController {

    private final ChatService chatService;
    private final AuthenticationService authenticationService;
    private final ChatRoomRepository chatRoomRepository;

    @Operation(summary = "개인 채팅방 생성 API")
    @PostMapping("/room/private/{groupId}")
    public ResponseEntity<ApiResponse<String>> createPrivateRoom(@PathVariable Long groupId, Principal principal) {
        String response = chatService.createPrivateRoom(groupId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "그룹 채팅방 생성 API")
    @PostMapping("/room/group/{groupId}")
    public ResponseEntity<ApiResponse<String>> createGroupRoom(@PathVariable Long groupId, Principal principal) {
        String response = chatService.createGroupRoom(groupId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "채팅방 정보 조회 API")
    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatRoomInfoDto>>> getChatRoomInfo(Principal principal) {
        List<ChatRoomInfoDto> response = chatService.getChatRoomInfo(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "채팅방 id 조회 API")
    @GetMapping("/room/group/{groupId}")
    public ResponseEntity<ApiResponse<Long>> getChatRoomByGroupId(@PathVariable Long groupId) {
        Long response = chatService.getChatRoomByStudyGroupId(groupId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "채팅 전송 API")
    @MessageMapping("/message")
    public void sendMessage(ChatMessageDto messageDto, StompHeaderAccessor headerAccessor) {
        Principal principal = headerAccessor.getUser();
        if (principal != null) {
            chatService.sendChatAndNotify(messageDto, principal.getName());
        }
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

    @Operation(summary = "채팅에 참여 중인 멤버 닉네임 조회 API")
    @GetMapping("/room/{roomId}/members")
    public ResponseEntity<ApiResponse<List<String>>> getOtherMemberNicknames(@PathVariable Long roomId,
                                                                             Principal principal) {
        Member currentMember = authenticationService.getCurrentMember(principal);
        ChatRoom room = chatRoomRepository.findById(roomId).orElse(null);

        List<Member> others = chatService.findOtherMembersInRoom(room, currentMember);
        List<String> nicknames = others.stream()
                .map(Member::getNickname)
                .toList();

        return ResponseEntity.ok(ApiResponse.onSuccess(nicknames));
    }

    @Operation(summary = "이미 생성된 채팅방 ID 조회 API")
    @GetMapping("/created-rooms")
    public ResponseEntity<ApiResponse<List<Long>>> getCreatedGroupIds(Principal principal) {
        List<Long> createdStudyGroupIds = chatService.getStudyGroupIdsWithChatRoom(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(createdStudyGroupIds));
    }

}