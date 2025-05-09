package com.api.meetudy.global.config;

import com.api.meetudy.auth.filter.JwtAuthenticationFilter;
import com.api.meetudy.auth.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();
                                configuration.setAllowedOrigins(
                                        Arrays.asList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);

                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        }));

        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable);

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable);

        httpSecurity
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        httpSecurity
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/**").permitAll()
                        .requestMatchers(
                                "/"
                                , "/swagger-ui/**"
                                , "/v3/api-docs/**"
                                , "/v2/swagger-config"
                                , "/swagger-resources/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/members/**").permitAll()
                        .requestMatchers("/oauth2/callback/kakao").permitAll()
                        .requestMatchers("/search/sort").hasRole("USER")
                        .requestMatchers("/search/filter", "/search").permitAll()
                        .requestMatchers("/study-groups/**").hasRole("USER")
                        .requestMatchers("/chats/**").hasRole("USER")
                        .requestMatchers("/my-page/**").hasRole("USER")
                        .requestMatchers("/study/recommendations").hasRole("USER")
                );

        httpSecurity
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider, redisTemplate),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}