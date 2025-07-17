package com.api.meetudy.comment.service;

import com.api.meetudy.comment.dto.CommentRequestDto;
import com.api.meetudy.comment.entity.Comment;
import com.api.meetudy.notification.service.NotificationService;
import com.api.meetudy.post.entity.Post;
import com.api.meetudy.comment.mapper.CommentMapper;
import com.api.meetudy.comment.repository.CommentRepository;
import com.api.meetudy.post.repository.PostRepository;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final NotificationService notificationService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CacheManager cacheManager;

    @Transactional
    @CacheEvict(value = "postDetails", key = "#postId")
    public String addComment(Long postId, CommentRequestDto commentRequestDto, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.POST_NOT_FOUND));

        Comment comment = commentMapper.toComment(commentRequestDto, member);
        comment.updatePost(post);
        commentRepository.save(comment);

        if (!member.equals(post.getAuthor())) {
            notificationService.sendCommentNotification(post.getAuthor(), post.getId(), comment);
        }

        return "Comment has been successfully created.";
    }

    @Transactional
    public String updateComment(Long commentId, CommentRequestDto commentRequestDto, Member member) {
        Comment comment = findCommentWithAuthorCheck(commentId, member.getId());
        comment.updateComment(commentRequestDto.getContent());
        commentRepository.save(comment);

        Long postId = comment.getPost().getId();

        Cache cache = cacheManager.getCache("postDetails");
        if (cache != null && postId != null) {
            cache.evict(postId);
        }

        return "Comment has been successfully updated.";
    }

    @Transactional
    public String deleteComment(Long commentId, Member member) {
        Comment comment = findCommentWithAuthorCheck(commentId, member.getId());
        Long postId = comment.getPost().getId();

        commentRepository.delete(comment);

        Cache cache = cacheManager.getCache("postDetails");
        if (cache != null && postId != null) {
            cache.evict(postId);
        }

        return "Comment has been successfully deleted.";
    }

    private Comment findCommentWithAuthorCheck(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorStatus.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(memberId)) {
            throw new CustomException(ErrorStatus.WRONG_POST_AUTHOR);
        }

        return comment;
    }

}