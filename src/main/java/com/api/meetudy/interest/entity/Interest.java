package com.api.meetudy.interest.entity;

import com.api.meetudy.group.enums.StudyCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Entity
@ToString
@Table(name  = "interest")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyCategory studyCategory;

    @OneToMany(mappedBy = "interest", cascade = CascadeType.ALL)
    private List<MemberInterest> memberInterests;

}