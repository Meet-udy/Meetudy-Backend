package com.api.meetudy.auth.service;

import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final MemberRepository memberRepository;

    public Member getCurrentMember(Principal principal) {
        String username = principal.getName();
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    public Member getCurrentMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND));
    }

}