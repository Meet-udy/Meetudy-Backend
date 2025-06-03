package com.api.meetudy.comment.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.comment.dto.CommentRequestDto;
import com.api.meetudy.comment.service.CommentService;
import com.api.meetudy.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetudy/comments")
public class CommentController {

    private final CommentService commentService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "댓글 작성 API")
    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<String>> addComment(@PathVariable Long postId,
                                                          @Valid @RequestBody CommentRequestDto commentRequestDto,
                                                          Principal principal) {
        String response = commentService.addComment(postId, commentRequestDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "댓글 수정 API")
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<String>> updateComment(@PathVariable Long commentId,
                                                             @RequestBody CommentRequestDto commentRequestDto,
                                                             Principal principal) {
        String response = commentService.updateComment(commentId, commentRequestDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "댓글 삭제 API")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long commentId,
                                                             Principal principal) {
        String response = commentService.deleteComment(commentId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}