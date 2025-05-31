package com.api.meetudy.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    @Schema(description = "The unique identifier of the comment.",
            example = "1")
    private Long commentId;

    @Schema(description = "Content of the comment to be created.",
            example = "I totally agree with your point.")
    private String content;

    @Schema(description = "Nickname of the comment's author.",
            example = "username")
    private String authorNickName;

    @Schema(description = "Timestamp when the comment was created.",
            example = "2025-05-31T14:22:00")
    private LocalDateTime createdAt;

}