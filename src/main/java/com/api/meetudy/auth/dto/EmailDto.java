package com.api.meetudy.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class EmailDto {

    @Email
    @Schema(description = "Email address for sending or verifying purposes.",
            example = "user@example.com")
    private String email;

}