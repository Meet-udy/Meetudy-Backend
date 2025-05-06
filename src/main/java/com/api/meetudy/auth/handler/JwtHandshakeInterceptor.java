package com.api.meetudy.auth.handler;

import com.api.meetudy.auth.provider.JwtTokenProvider;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@AllArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            String token = req.getParameter("token");

            if (token == null) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                response.getHeaders().add("Error", "Token is missing");
                return false;
            }

            if (!jwtTokenProvider.validateToken(token)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().add("Error", "Invalid token");
                return false;
            }

            String username = jwtTokenProvider.getUsernameFromToken(token);

            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        response.setStatusCode(HttpStatus.NOT_FOUND);
                        response.getHeaders().add("Error", "Member not found for email: " + username);
                        return new RuntimeException("Member not found");
                    });

            attributes.put("username", username);
            attributes.put("principal", member);
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
    }

}