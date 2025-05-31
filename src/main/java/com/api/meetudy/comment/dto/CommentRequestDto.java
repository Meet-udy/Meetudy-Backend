package com.api.meetudy.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @Schema(description = "Content of the comment to be updated.",
            example = "I totally agree with your point.")
    private String content;

}