package com.api.meetudy.search.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.group.dto.StudyGroupDto;
import com.api.meetudy.group.enums.Location;
import com.api.meetudy.group.enums.StudyCategory;
import com.api.meetudy.search.service.StudyGroupFilterService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetudy/study-groups/filter")
public class StudyGroupFilterController {

    private final StudyGroupFilterService filterService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "스터디 그룹 필터링 API")
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> filterStudyGroups(@RequestParam(required = false) String maxParticipantsCondition,
                                                                              @RequestParam(required = false) Boolean isOnline,
                                                                              @RequestParam(required = false) StudyCategory category,
                                                                              @RequestParam(required = false) Location location) {
        List<StudyGroupDto> response = filterService.filterStudyGroups(maxParticipantsCondition, isOnline, category, location);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹 정렬 API")
    @GetMapping("/sort")
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> sortStudyGroups(@RequestParam(defaultValue = "LATEST") String sortBy,
                                                                            Principal principal) {
        List<StudyGroupDto> response = filterService.sortStudyGroups(sortBy, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}
