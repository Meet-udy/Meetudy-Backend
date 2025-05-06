package com.api.meetudy.member.controller;

import com.api.meetudy.auth.dto.EmailDto;
import com.api.meetudy.auth.dto.JwtTokenDto;
import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.auth.service.EmailService;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.dto.*;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.member.repository.MemberRepository;
import com.api.meetudy.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final AuthenticationService authenticationService;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_PARAM = "refreshToken";
    public static final String BEARER_PREFIX = "Bearer ";

    @Operation(summary = "회원가입 API")
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<String>> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        String response = memberService.signUp(signUpDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "로그인 API")
    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<JwtTokenDto>> signIn(@Valid @RequestBody SignInDto signInDto) {
        JwtTokenDto response = memberService.signIn(signInDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "카카오 로그인 API")
    @PostMapping("/kakao/login")
    public ResponseEntity<ApiResponse<KakaoLoginDto>> kakaoLogin(@RequestParam String code) {
        KakaoLoginDto response = memberService.signInWithKakao(code);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "카카오 로그인 시 추가 정보 입력 API")
    @PostMapping("/additional-info")
    public ResponseEntity<ApiResponse<String>> updateAdditionalInfo(@Valid @RequestBody AdditionalInfoDto additionalInfoDto,
                                                                    Principal principal) {
        String response = memberService.updateAdditionalInfo(additionalInfoDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "로그아웃 API")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader(AUTHORIZATION_HEADER) String accessToken) {
        String token = accessToken.replace(BEARER_PREFIX, "");
        String response = memberService.logout(token);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "토큰 갱신 API")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtTokenDto>> refreshToken(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                                 @RequestParam(REFRESH_TOKEN_PARAM) String refreshToken) {
        String token = accessToken.replace(BEARER_PREFIX, "");
        JwtTokenDto response = memberService.refreshToken(token, refreshToken);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "아이디 찾기 API")
    @PostMapping("/username")
    public ResponseEntity<ApiResponse<String>> findUsername(@Valid @RequestBody EmailDto emailDto) {
        Member member = memberRepository.findByEmail(emailDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));
        return ResponseEntity.ok(ApiResponse.onSuccess(member.getUsername()));
    }

    @Operation(summary = "비밀번호 찾기 API")
    @PostMapping("/password")
    public ResponseEntity<ApiResponse<String>> findPassword(@Valid @RequestBody PasswordFindingDto findingDto) {
        try {
            String response = emailService.sendPasswordResetEmail(findingDto.getUsername(), findingDto.getEmail());
            return ResponseEntity.ok(ApiResponse.onSuccess(response));
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.SEND_EMAIL_FAILED);
        }
    }

    @Operation(summary = "아이디 중복 확인 API")
    @PostMapping("/username/duplication")
    public ResponseEntity<ApiResponse<Boolean>> checkUsernameDuplication(@Valid @RequestBody UsernameDuplicationDto duplicationDto) {
        boolean isUsernameExist = memberRepository.existsByUsername(duplicationDto.getUsername());
        return ResponseEntity.ok(ApiResponse.onSuccess(!isUsernameExist));
    }

    @Operation(summary = "닉네임 중복 확인 API")
    @GetMapping("/nickname/duplication")
    public ResponseEntity<ApiResponse<Boolean>> checkNicknameDuplication(@RequestParam String nickname) {
        boolean isNicknameExist = memberRepository.existsByNickname(nickname);
        return ResponseEntity.ok(ApiResponse.onSuccess(!isNicknameExist));
    }

    @Operation(summary = "로그인한 사용자의 memberId 반환 API")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Long>> getCurrentMemberId(Principal principal) {
        Member member = authenticationService.getCurrentMember(principal);
        return ResponseEntity.ok(ApiResponse.onSuccess(member.getId()));
    }

}