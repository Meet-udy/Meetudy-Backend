package com.api.meetudy.post.dto;

import com.api.meetudy.comment.dto.CommentDto;
import com.api.meetudy.post.enums.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostDetailDto {

    @Schema(description = "The unique identifier of the post.",
            example = "1")
    private Long postId;

    @Schema(description = "Title of the post to be created.",
            example = "Looking for study group members.")
    private String title;

    @Schema(description = "Content of the post to be created.",
            example = "We're starting a Java study group. Join us.")
    private String content;

    @Schema(description = "Category of the post to be created.",
            example = "STUDY_PROMOTION, STUDY_QUESTION")
    private PostCategory postCategory;

    @Schema(description = "Nickname of the author who created the post.",
            example = "username")
    private String authorNickname;

    @Schema(description = "Timestamp when the post was created.",
            example = "2025-05-31T14:22:00")
    private LocalDateTime createdAt;

    @Schema(description = "Indicates if the current user is the author of the post.",
            example = "true")
    private Boolean isMyPost;

    @Schema(description = "List of comments associated with the post.")
    private List<CommentDto> comments;

}