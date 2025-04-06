package com.api.meetudy.study.group.dto;

import com.api.meetudy.study.group.enums.StudyCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyGroupMemberDto {

    private Long groupMemberId;

    private String nickname;

    private String major;

    private String introduction;

    private Integer activityScore;

    private List<StudyCategory> interests;

}
