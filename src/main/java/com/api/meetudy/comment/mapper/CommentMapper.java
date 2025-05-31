package com.api.meetudy.comment.mapper;

import com.api.meetudy.comment.dto.CommentDto;
import com.api.meetudy.comment.dto.CommentRequestDto;
import com.api.meetudy.comment.entity.Comment;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.post.entity.Post;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    Comment toComment(CommentRequestDto commentRequestDto, @Context Member author);

    @Mapping(target = "commentId", source = "id")
    @Mapping(target = "authorNickName", source = "author.nickname")
    CommentDto toCommentDto(Comment comment);

    @IterableMapping(elementTargetType = CommentDto.class)
    List<CommentDto> toCommentDtoList(List<Comment> comments);

    @AfterMapping
    default void setAuthor(@MappingTarget Comment comment, @Context Member author) {
        comment.updateAuthor(author);
    }

}