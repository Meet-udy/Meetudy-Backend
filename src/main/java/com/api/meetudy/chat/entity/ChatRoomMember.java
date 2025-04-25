package com.api.meetudy.chat.entity;

import com.api.meetudy.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@ToString
@Table(name  = "chat_room_member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String displayName;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public ChatRoomMember(ChatRoom chatRoom, Member member) {
        this.chatRoom = chatRoom;
        this.member = member;
    }

    public static ChatRoomMember of(String displayName, ChatRoom room, Member member) {
        return ChatRoomMember.builder()
                .displayName(displayName)
                .chatRoom(room)
                .member(member)
                .build();
    }

}
