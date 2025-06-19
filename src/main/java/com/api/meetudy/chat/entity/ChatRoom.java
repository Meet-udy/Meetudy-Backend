package com.api.meetudy.chat.entity;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.group.entity.StudyGroup;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@Entity
@Table(name  = "chat_room")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String groupName;

    @Column(nullable = false)
    private Boolean isPrivate;

    @Builder.Default
    @JsonManagedReference
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomMember> members = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    public void addChatRoomMember(ChatRoomMember member) {
        if (this.members == null) {
            this.members = new HashSet<>();
        }

        this.members.add(member);
    }

    public void updateStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public void updateGroupName(String newName) {
        this.groupName = newName;
    }

    public static ChatRoom createPrivateRoom(Member currentUser, Member targetUser) {
        ChatRoom chatRoom = ChatRoom.builder()
                .groupName(null)
                .isPrivate(true)
                .build();

        chatRoom.addChatRoomMember(ChatRoomMember.of(targetUser.getNickname(), chatRoom, currentUser));
        chatRoom.addChatRoomMember(ChatRoomMember.of(currentUser.getNickname(), chatRoom, targetUser));

        return chatRoom;
    }

    public static ChatRoom createGroupRoom(Set<Member> members) {
        String groupName = members.stream()
                .map(Member::getNickname)
                .collect(Collectors.joining(", "));

        ChatRoom chatRoom = ChatRoom.builder()
                .isPrivate(false)
                .groupName(groupName)
                .build();

        for (Member member : members) {
            if (chatRoom.getMembers().stream().noneMatch(m -> m.getMember().equals(member))) {
                chatRoom.addChatRoomMember(ChatRoomMember.of(groupName, chatRoom, member));
            }
        }

        return chatRoom;
    }

}