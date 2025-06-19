package com.api.meetudy.search.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.group.dto.StudyGroupDto;
import com.api.meetudy.search.dto.AutoCompleteDto;
import com.api.meetudy.search.service.StudyGroupSearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetudy/study-groups/search")
public class StudyGroupSearchController {

    private final StudyGroupSearchService searchService;
    private final AuthenticationService authenticationService;

    /*@Operation(summary = "스터디 그룹 검색 API")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> searchStudyGroups(@RequestParam String keyword) {
        List<StudyGroupDto> response = searchService.searchStudyGroups(keyword);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }*/

    @Operation(summary = "검색 자동 완성 API")
    @GetMapping("/autocomplete")
    public ResponseEntity<ApiResponse<AutoCompleteDto>> autocomplete(@RequestParam("query") String query) {
        AutoCompleteDto response = searchService.getAutoCompleteSuggestions(query);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "카테고리 검색 API")
    @GetMapping("/category")
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> searchByCategory(@RequestParam("category") String categoryKor) {
        List<StudyGroupDto> response = searchService.searchByCategory(categoryKor);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹 이름 검색 API")
    @GetMapping("/group")
    public ResponseEntity<ApiResponse<StudyGroupDto>> searchByGroupName(@RequestParam("name") String groupName) {
        StudyGroupDto response = searchService.searchByExactName(groupName);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}