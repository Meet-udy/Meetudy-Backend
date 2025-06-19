package com.api.meetudy.group.entity;

import com.api.meetudy.chat.entity.ChatRoom;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.group.enums.GroupMemberStatus;
import com.api.meetudy.group.enums.Location;
import com.api.meetudy.group.enums.StudyCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name  = "study_group")
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false)
    private Boolean isOnline;

    @Column(nullable = false)
    private Boolean isRecruiting;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Location location;

    @OneToMany(mappedBy = "studyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyGroupMember> members = new ArrayList<>();

    @OneToOne(mappedBy = "studyGroup", fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    public void closeRecruitment() {
        this.isRecruiting = false;
    }

    @PrePersist
    public void prePersist() {
        if (isRecruiting == null) {
            isRecruiting = true;
        }
    }

    public StudyGroupMember getLeader() {
        return members.stream()
                .filter(member -> member.getStatus() == GroupMemberStatus.LEADER)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND));
    }

}