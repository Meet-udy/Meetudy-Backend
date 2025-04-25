package com.api.meetudy.chat.repository;

import com.api.meetudy.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


}
