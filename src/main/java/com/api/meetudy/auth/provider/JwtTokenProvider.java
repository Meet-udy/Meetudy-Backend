package com.api.meetudy.auth.provider;

import com.api.meetudy.auth.dto.JwtTokenDto;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    public static final long ACCESS_TIME = Duration.ofMinutes(30).toMillis(); // 만료시간 30분
    public static final long REFRESH_TIME = Duration.ofDays(14).toMillis(); // 만료시간 2주

    public static final String AUTH_CLAIM = "auth";
    public static final String BEARER_PREFIX = "Bearer";

    public static final String INVALID_JWT_SIGNATURE = "Invalid JWT signature";
    public static final String EXPIRED_JWT = "Expired JWT";
    public static final String UNSUPPORTED_JWT = "Unsupported JWT";
    public static final String JWT_CLAIMS_EMPTY = "JWT claims is empty";

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtTokenDto generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + ACCESS_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TIME);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTH_CLAIM, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtTokenDto.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(REFRESH_TIME)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = extractClaims(accessToken);

        if(claims.get(AUTH_CLAIM) == null) {
            throw new CustomException(ErrorStatus.TOKEN_INVALID);
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTH_CLAIM).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error(INVALID_JWT_SIGNATURE);
            return false;
        } catch (ExpiredJwtException e) {
            log.error(EXPIRED_JWT);
            return false;
        } catch (UnsupportedJwtException e) {
            log.error(UNSUPPORTED_JWT);
            return false;
        } catch (IllegalArgumentException e) {
            log.error(JWT_CLAIMS_EMPTY);
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Long getExpiration(String accessToken) {
        Date expiration = extractClaims(accessToken).getExpiration();
        Long now = new Date().getTime();

        return (expiration.getTime() - now);
    }

    public String getUsernameFromToken(String accessToken) {
        Claims claims = extractClaims(accessToken);
        return claims.getSubject();
    }

    private Claims extractClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}