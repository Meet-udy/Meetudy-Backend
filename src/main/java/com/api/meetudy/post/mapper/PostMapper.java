package com.api.meetudy.post.mapper;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.post.dto.PostDto;
import com.api.meetudy.post.dto.PostRequestDto;
import com.api.meetudy.post.entity.Post;
import org.mapstruct.*;

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
    PostDto toPostDto(Post post);

    @IterableMapping(elementTargetType = PostDto.class)
    List<PostDto> toPostDtoList(List<Post> posts);

    @AfterMapping
    default void setAuthor(@MappingTarget Post post, @Context Member author) {
        post.updateAuthor(author);
    }

}