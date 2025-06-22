package com.api.meetudy.notification.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.notification.dto.NotificationDto;
import com.api.meetudy.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetudy/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam("token") String token) {
        return notificationService.subscribe(token);
    }

    @Operation(summary = "알림 전체 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getAllNotifications(Principal principal) {
        List<NotificationDto> response = notificationService.getAllNotifications(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "알림 읽음 표시 API")
    @PatchMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable Long notificationId,
                                                          Principal principal) {
        String response = notificationService.markAsRead(notificationId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}