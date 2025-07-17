package com.api.meetudy.post.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.post.dto.PostDetailDto;
import com.api.meetudy.post.dto.PostDto;
import com.api.meetudy.post.dto.PostRequestDto;
import com.api.meetudy.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetudy/posts")
public class PostController {

    private final PostService postService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "게시글 작성 API")
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createPost(@Valid @RequestBody PostRequestDto postRequestDto, Principal principal) {
        String response = postService.createPost(postRequestDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "게시글 수정 API")
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailDto>> updatePost(@PathVariable Long postId,
                                                          @Valid @RequestBody PostRequestDto postRequestDto,
                                                          Principal principal) {
        PostDetailDto response = postService.updatePost(postId, postRequestDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "게시글 삭제 API")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable Long postId, Principal principal) {
        String response = postService.deletePost(postId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "전체 게시글 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDto>>> getAllPosts() {
        List<PostDto> response = postService.getAllPosts();
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "특정 게시글 상세 정보 조회 API")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailDto>> getPostById(@PathVariable Long postId,
                                                                  Principal principal) {
        PostDetailDto response = postService.getPostById(postId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "특정 사용자의 게시글 조회 API")
    @GetMapping("/member")
    public ResponseEntity<ApiResponse<List<PostDto>>> getPostsByMember(Principal principal) {
        List<PostDto> response = postService.getPostsByMember(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}