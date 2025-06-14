package com.api.meetudy.post.mapper;

import com.api.meetudy.comment.dto.CommentDto;
import com.api.meetudy.comment.entity.Comment;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.post.dto.PostDetailDto;
import com.api.meetudy.post.dto.PostDto;
import com.api.meetudy.post.dto.PostRequestDto;
import com.api.meetudy.post.entity.Post;
import org.mapstruct.*;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "author", ignore = true)
    Post toPost(PostRequestDto postRequestDto, @Context Member author);

    @Mapping(target = "postId", source = "id")
    @Mapping(target = "authorNickname", source = "author.nickname")
    @Mapping(expression = "java(post.getComments() != null ? post.getComments().size() : 0)", target = "commentCount")
    PostDto toPostDto(Post post);

    @Mapping(target = "postId", source = "id")
    @Mapping(target = "authorNickname", source = "author.nickname")
    @Mapping(target = "comments", ignore = true)
    PostDetailDto toPostDetailDto(Post post);

    @IterableMapping(elementTargetType = PostDto.class)
    List<PostDto> toPostDtoList(List<Post> posts);

    @AfterMapping
    default void setAuthor(@MappingTarget Post post, @Context Member author) {
        post.updateAuthor(author);
    }

    default PostDetailDto toPostDetailDtoWithSortedComments(Post post, Long currentMemberId) {
        PostDetailDto postDetailDto = toPostDetailDto(post);
        postDetailDto.setIsMyPost(post.getAuthor().getId().equals(currentMemberId));


        List<CommentDto> comments = post.getComments().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .map(comment -> CommentDto.builder()
                        .commentId(comment.getId())
                        .content(comment.getContent())
                        .authorNickname(comment.getAuthor().getNickname())
                        .createdAt(comment.getCreatedAt())
                        .isMyComment(comment.getAuthor().getId().equals(currentMemberId))
                        .build())
                .toList();

        postDetailDto.setComments(comments);
        return postDetailDto;
    }

}