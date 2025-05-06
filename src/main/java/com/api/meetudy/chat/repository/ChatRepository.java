package com.api.meetudy.chat.repository;

import com.api.meetudy.chat.entity.Chat;
import com.api.meetudy.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByRoom_IdOrderByCreatedAtAsc(Long roomId);

    Optional<Chat> findTopByRoomOrderByCreatedAtDesc(ChatRoom room);

}