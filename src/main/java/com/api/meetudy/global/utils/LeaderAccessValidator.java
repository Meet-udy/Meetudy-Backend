package com.api.meetudy.global.utils;

import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.group.entity.StudyGroup;
import com.api.meetudy.group.entity.StudyGroupMember;
import com.api.meetudy.group.enums.GroupMemberStatus;
import org.springframework.stereotype.Component;

@Component
public class LeaderAccessValidator {

    public void checkLeaderAccess(Member member, StudyGroup studyGroup) {
        StudyGroupMember leader = studyGroup.getMembers().stream()
                .filter(memberInGroup -> memberInGroup.getStatus() == GroupMemberStatus.LEADER)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND));

        if (!leader.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorStatus.GROUP_LEADER_ACCESS_ONLY);
        }
    }

}