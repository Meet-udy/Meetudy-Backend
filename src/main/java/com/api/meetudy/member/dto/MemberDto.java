package com.api.meetudy.member.dto;

import com.api.meetudy.group.enums.Location;
import com.api.meetudy.group.enums.StudyCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberDto {

    private String email;

    private String username;

    private String nickname;

    private String major;

    private String introduction;

    // private String profileImage;

    private Integer activityScore;

    private Boolean notificationEnabled;

    private Location location;

    private List<StudyCategory> interests;

    public void setInterests(List<StudyCategory> interests) {
        this.interests = interests;
    }

}
