package com.api.meetudy.chat.repository;

import com.api.meetudy.chat.entity.ChatRoom;
import com.api.meetudy.chat.entity.ChatRoomMember;
import com.api.meetudy.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    Optional<ChatRoomMember> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

}
