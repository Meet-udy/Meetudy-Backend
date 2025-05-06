package com.api.meetudy.chat.entity;

import com.api.meetudy.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name  = "chat_room_member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String displayName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static ChatRoomMember of(String displayName, ChatRoom chatRoom, Member member) {
        return ChatRoomMember.builder()
                .displayName(displayName)
                .chatRoom(chatRoom)
                .member(member)
                .build();
    }

}