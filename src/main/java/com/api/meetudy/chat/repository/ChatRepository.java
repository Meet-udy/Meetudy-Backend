package com.api.meetudy.chat.repository;

import com.api.meetudy.chat.entity.Chat;
import com.api.meetudy.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByRoom(ChatRoom room);

}
