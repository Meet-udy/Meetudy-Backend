package com.api.meetudy.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JwtTokenDto {

    private String grantType;

    private String accessToken;

    private String refreshToken;

    private Long refreshTokenExpiresIn;

}