package com.api.meetudy.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VerificationDto {

    @Email
    @Schema(description = "Email address for sending or verifying purposes.",
            example = "user@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Verification code sent to the email.",
            example = "123456")
    private String code;

}