package com.api.meetudy.mypage.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.member.dto.MemberDto;
import com.api.meetudy.member.dto.MemberUpdateDto;
import com.api.meetudy.mypage.service.MyPageService;
import com.api.meetudy.study.group.dto.StudyGroupUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetudy/my-page")
public class MyPageController {

    private final MyPageService myPageService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "사용자 프로필 조회 API")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<MemberDto>> getMemberProfile(Principal principal) {
        MemberDto message = myPageService.getMemberProfile(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "사용자 프로필 수정 API")
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<String>> updateMember(@Valid @RequestBody MemberUpdateDto memberUpdateDto,
                                                               Principal principal) {
        String message = myPageService.updateMember(memberUpdateDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "스터디 그룹 정보 수정 API")
    @PutMapping("/group/{groupId}")
    public ResponseEntity<ApiResponse<String>> updateGroupInfo(@Valid @RequestBody StudyGroupUpdateDto groupUpdateDto,
                                                               @PathVariable Long groupId,
                                                               Principal principal) {
        String message = myPageService.updateGroupInfo(groupId, groupUpdateDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "스터디 그룹에서 멤버 제거 API")
    @DeleteMapping("/group/{groupId}/member/{memberId}")
    public ResponseEntity<ApiResponse<String>> removeMember(@PathVariable Long groupId,
                                                            @PathVariable Long memberId,
                                                            Principal principal) {
        String message = myPageService.removeMember(groupId, memberId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "스터디 그룹 리더 권한 위임 API")
    @PutMapping("/group/{groupId}/member/{newLeaderId}")
    public ResponseEntity<ApiResponse<String>> delegateLeader(@PathVariable Long groupId,
                                                              @PathVariable Long newLeaderId,
                                                              Principal principal) {
        String message = myPageService.delegateLeader(groupId, newLeaderId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "스터디 그룹 탈퇴 API")
    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<ApiResponse<String>> leaveGroup(@PathVariable Long groupId,
                                                          Principal principal) {
        String message = myPageService.leaveGroup(groupId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

}