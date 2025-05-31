package com.api.meetudy.post.repository;

import com.api.meetudy.post.entity.Post;
import com.api.meetudy.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtAsc();

    List<Post> findByAuthorOrderByCreatedAtAsc(Member member);

}