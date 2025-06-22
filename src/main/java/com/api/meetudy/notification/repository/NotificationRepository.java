package com.api.meetudy.notification.repository;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverOrderByCreatedAtDesc(Member member);

}