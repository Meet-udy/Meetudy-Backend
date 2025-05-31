package com.api.meetudy.comment.service;

import com.api.meetudy.comment.dto.CommentDto;
import com.api.meetudy.comment.dto.CommentRequestDto;
import com.api.meetudy.comment.entity.Comment;
import com.api.meetudy.post.entity.Post;
import com.api.meetudy.comment.mapper.CommentMapper;
import com.api.meetudy.comment.repository.CommentRepository;
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
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public String addComment(Long postId, CommentRequestDto commentRequestDto, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.POST_NOT_FOUND));

        Comment comment = commentMapper.toComment(commentRequestDto, member);
        comment.updatePost(post);
        commentRepository.save(comment);

        return "Comment has been successfully created.";
    }

    @Transactional
    public String updateComment(Long commentId, CommentRequestDto commentRequestDto, Member member) {
        Comment comment = findCommentWithAuthorCheck(commentId, member.getId());
        comment.updateComment(commentRequestDto.getContent());

        commentRepository.save(comment);
        return "Comment has been successfully updated.";
    }

    @Transactional
    public String deleteComment(Long commentId, Member member) {
        Comment comment = findCommentWithAuthorCheck(commentId, member.getId());
        commentRepository.delete(comment);

        return "Comment has been successfully deleted.";
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.POST_NOT_FOUND));

        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtAsc(post);
        return commentMapper.toCommentDtoList(comments);
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