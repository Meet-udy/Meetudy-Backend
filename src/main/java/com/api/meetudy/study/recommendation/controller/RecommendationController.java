package com.api.meetudy.study.recommendation.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.study.group.dto.StudyGroupDto;
import com.api.meetudy.study.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetudy/recommendations")
public class RecommendationController {

    private final AuthenticationService authenticationService;
    private final RecommendationService recommendationService;

    @Operation(summary = "스터디 그룹 추천 API")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> requestJoinGroup(Principal principal) {
        List<StudyGroupDto> message = recommendationService.recommendStudyGroups(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

}