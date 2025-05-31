package com.api.meetudy.auth.controller;

import com.api.meetudy.auth.dto.EmailDto;
import com.api.meetudy.auth.dto.VerificationDto;
import com.api.meetudy.auth.service.EmailService;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetudy/auth")
public class AuthController {

    private final EmailService emailService;

    @Operation(summary = "본인 인증 메일 전송 API")
    @PostMapping("/send/verification")
    public ResponseEntity<ApiResponse<String>> sendVerificationEmail(@Valid @RequestBody EmailDto emailDto) {
        try {
            String response = emailService.sendVerificationEmail(emailDto.getEmail());
            return ResponseEntity.ok(ApiResponse.onSuccess(response));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorStatus.SEND_EMAIL_FAILED);
        }
    }

    @Operation(summary = "인증 번호 검증 API")
    @PostMapping("/verification")
    public ResponseEntity<ApiResponse<String>> verifyCode(@Valid @RequestBody VerificationDto verificationDto) {
        boolean isVerified = emailService.verifyCode(verificationDto.getEmail(), verificationDto.getCode());

        if(isVerified) {
            return ResponseEntity.ok(ApiResponse.onSuccess("Verification successful"));
        } else {
            throw  new CustomException(ErrorStatus.VERIFICATION_FAILED);
        }
    }

}