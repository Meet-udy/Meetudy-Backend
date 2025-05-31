package com.api.meetudy.comment.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.comment.dto.CommentDto;
import com.api.meetudy.comment.dto.CommentRequestDto;
import com.api.meetudy.comment.service.CommentService;
import com.api.meetudy.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetudy/comments")
public class CommentController {

    private final CommentService commentService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "댓글 작성 API")
    @PostMapping
    public ResponseEntity<ApiResponse<String>> addComment(Long postId,
                                                          @Valid @RequestBody CommentRequestDto commentRequestDto,
                                                          Principal principal) {
        String response = commentService.addComment(postId, commentRequestDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "댓글 수정 API")
    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateComment(Long commentId,
                                                             @RequestBody CommentRequestDto commentRequestDto,
                                                             Principal principal) {
        String response = commentService.updateComment(commentId, commentRequestDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "댓글 삭제 API")
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteComment(Long commentId, Principal principal) {
        String response = commentService.deleteComment(commentId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "특정 게시물의 댓글 조회 API")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<List<CommentDto>>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDto> response = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}