package com.api.meetudy.chat.repository;

import com.api.meetudy.chat.entity.ChatRoom;
import com.api.meetudy.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findAllByMembers_Member(Member member);

    Optional<ChatRoom> findByStudyGroup_Id(Long groupId);

    @Query("SELECT r FROM ChatRoom r LEFT JOIN FETCH r.members m WHERE r.id = :id")
    Optional<ChatRoom> findByIdWithMembers(@Param("id") Long id);

}