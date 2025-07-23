package com.api.meetudy.group.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.group.dto.StudyGroupDto;
import com.api.meetudy.group.dto.StudyGroupMemberDto;
import com.api.meetudy.group.dto.StudyGroupUpdateDto;
import com.api.meetudy.group.enums.GroupMemberStatus;
import com.api.meetudy.group.service.StudyGroupService;
import com.api.meetudy.group.service.GroupManagementService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetudy/study-groups")
public class StudyGroupController {

    private final AuthenticationService authenticationService;
    private final StudyGroupService studyGroupService;
    private final GroupManagementService groupManagementService;

    @Operation(summary = "스터디 그룹 가입 요청 API")
    @PostMapping("/{groupId}/join-requests")
    public ResponseEntity<ApiResponse<String>> requestJoinGroup(@PathVariable Long groupId,
                                                                Principal principal) {
        String message = studyGroupService.requestJoinGroup(groupId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "스터디 그룹 생성 API")
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createStudyGroup(@Valid @RequestBody StudyGroupDto studyGroupDto,
                                                                Principal principal) {
        String response = groupManagementService.createStudyGroup(studyGroupDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹 정보 수정 API")
    @PatchMapping("/{groupId}")
    public ResponseEntity<ApiResponse<String>> updateGroupInfo(@Valid @RequestBody StudyGroupUpdateDto groupUpdateDto,
                                                               @PathVariable Long groupId,
                                                               Principal principal) {
        String message = groupManagementService.updateGroupInfo(groupId, groupUpdateDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "스터디 그룹 가입 요청 승인 API")
    @PutMapping("/{groupMemberId}/approval")
    public ResponseEntity<ApiResponse<String>> approveJoinRequest(@PathVariable Long groupMemberId,
                                                                  Principal principal) {
        String response = groupManagementService.approveJoinRequest(groupMemberId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹 가입 요청 거절 API")
    @PutMapping("/{groupMemberId}/rejection")
    public ResponseEntity<ApiResponse<String>> rejectJoinRequest(@PathVariable Long groupMemberId,
                                                                 Principal principal) {
        String response = groupManagementService.rejectJoinRequest(groupMemberId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹 모집 상태 변경 API")
    @PatchMapping("/{groupId}/recruitment")
    public ResponseEntity<ApiResponse<StudyGroupDto>> updateRecruitmentStatus(@PathVariable Long groupId,
                                                                              @RequestParam boolean isRecruiting,
                                                                              Principal principal) {
        StudyGroupDto response = groupManagementService.updateRecruitmentStatus(groupId, isRecruiting, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "사용자에 대한 모든 스터디 그룹 조회 API")
    @GetMapping("/my-groups")
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> getAllStudyGroupsWithStatus(Principal principal) {
        List<StudyGroupDto> response = studyGroupService.getAllStudyGroupsWithMyStatus(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "가입 요청을 보낸 스터디 그룹 조회 API")
    @GetMapping("/pending-groups")
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> getPendingStudyGroups(Principal principal) {
        List<StudyGroupDto> response = studyGroupService.getPendingStudyGroups(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "특정 스터디 그룹 조회 API")
    @GetMapping("/{groupId}")
    public ResponseEntity<ApiResponse<StudyGroupDto>> getStudyGroupById(@PathVariable Long groupId, Principal principal) {
        StudyGroupDto response = studyGroupService.getStudyGroupById(groupId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "특정 스터디 그룹의 멤버 조회 API")
    @GetMapping("/{groupId}/members")
    public ResponseEntity<ApiResponse<List<StudyGroupMemberDto>>> getMembersByStatus(@PathVariable Long groupId,
                                                                                     @RequestParam GroupMemberStatus status,
                                                                                     Principal principal) {
        List<StudyGroupMemberDto> response = groupManagementService.getMembersByStatus(groupId, status, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "리더가 스터디 그룹 멤버 탈퇴시키는 API")
    @DeleteMapping("/{groupId}/member/{groupMemberId}")
    public ResponseEntity<ApiResponse<String>> removeMember(@PathVariable Long groupId,
                                                            @PathVariable Long groupMemberId,
                                                            Principal principal) {
        String response = groupManagementService.removeMember(groupId, groupMemberId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹 탈퇴 API")
    @DeleteMapping("/{groupId}/member")
    public ResponseEntity<ApiResponse<String>> leaveStudyGroup(@PathVariable Long groupId,
                                                               Principal principal) {
        String response = studyGroupService.leaveStudyGroup(groupId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}