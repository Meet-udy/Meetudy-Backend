package com.api.meetudy.group.entity;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.group.enums.GroupMemberStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@ToString
@Table(name  = "study_group_member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private GroupMemberStatus status = GroupMemberStatus.REQUESTED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    public void updateStatus(GroupMemberStatus status) {
        this.status = status;
    }

}