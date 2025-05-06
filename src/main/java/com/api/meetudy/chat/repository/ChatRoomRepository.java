package com.api.meetudy.chat.repository;

import com.api.meetudy.chat.entity.ChatRoom;
import com.api.meetudy.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findAllByMembers_Member(Member member);

}