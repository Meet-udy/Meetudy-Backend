package com.api.meetudy.post.dto;

import com.api.meetudy.post.enums.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PostRequestDto {

    @Schema(description = "Title of the post to be updated.",
            example = "Looking for study group members.")
    private String title;

    @Schema(description = "Content of the post to be updated.",
            example = "We're starting a Java study group. Join us.")
    private String content;

    @Schema(description = "Category of the post to be updated.",
            example = "STUDY_PROMOTION, STUDY_QUESTION")
    private PostCategory postCategory;

}