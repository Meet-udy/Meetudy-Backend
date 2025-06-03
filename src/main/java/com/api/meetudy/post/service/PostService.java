package com.api.meetudy.post.service;

import com.api.meetudy.post.dto.PostDetailDto;
import com.api.meetudy.post.dto.PostDto;
import com.api.meetudy.post.dto.PostRequestDto;
import com.api.meetudy.post.entity.Post;
import com.api.meetudy.post.mapper.PostMapper;
import com.api.meetudy.post.repository.PostRepository;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Transactional
    public String createPost(PostRequestDto postRequestDto, Member member) {
        Post post = postMapper.toPost(postRequestDto, member);
        postRepository.save(post);

        return "Post has been successfully created.";
    }

    @Transactional
    public String updatePost(Long postId, PostRequestDto postRequestDto, Member member) {
        Post post = findPostWithAuthorCheck(postId, member.getId());
        post.updatePost(postRequestDto.getTitle(), postRequestDto.getContent(), postRequestDto.getPostCategory());

        postRepository.save(post);
        return "Post has been successfully updated.";
    }

    @Transactional
    public String deletePost(Long postId, Member member) {
        Post post = findPostWithAuthorCheck(postId, member.getId());
        postRepository.delete(post);

        return "Post has been successfully deleted.";
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtAsc();
        return postMapper.toPostDtoList(posts);
    }

    @Transactional(readOnly = true)
    public PostDetailDto getPostById(Long postId, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.POST_NOT_FOUND));
        return postMapper.toPostDetailDtoWithSortedComments(post, member.getId());
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostsByMember(Member member) {
        List<Post> posts = postRepository.findByAuthorOrderByCreatedAtAsc(member);
        return postMapper.toPostDtoList(posts);
    }

    private Post findPostWithAuthorCheck(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.POST_NOT_FOUND));

        if (!post.getAuthor().getId().equals(memberId)) {
            throw new CustomException(ErrorStatus.WRONG_POST_AUTHOR);
        }

        return post;
    }

}