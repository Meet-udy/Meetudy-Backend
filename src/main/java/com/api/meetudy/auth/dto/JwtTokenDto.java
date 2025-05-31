package com.api.meetudy.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JwtTokenDto {

    @Schema(description = "Type of the token grant, typically 'Bearer'.",
            example = "Bearer")
    private String grantType;

    @Schema(description = "Access token string used for authentication.",
            example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String accessToken;

    @Schema(description = "Refresh token string used to obtain a new access token.",
            example = "dGhpc2lzcmVmcmVzaHRva2Vu")
    private String refreshToken;

    @Schema(description = "Expiration time of the refresh token in milliseconds.",
            example = "86400000")
    private Long refreshTokenExpiresIn;

}