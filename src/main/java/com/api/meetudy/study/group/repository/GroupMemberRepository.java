package com.api.meetudy.study.group.repository;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.enums.GroupMemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<StudyGroupMember, Long> {

    List<StudyGroupMember> findByMember(Member member);

    boolean existsByStudyGroupAndMember(StudyGroup studyGroup, Member member);

    long countByStudyGroupAndStatus(StudyGroup studyGroup, GroupMemberStatus groupMemberStatus);

    Optional<StudyGroupMember> findByMemberIdAndStudyGroupId(Long memberId, Long groupId);

    List<StudyGroupMember> findByMemberAndStatus(Member member, GroupMemberStatus groupMemberStatus);

    List<StudyGroupMember> findByStudyGroupIdAndStatus(Long groupId, GroupMemberStatus groupMemberStatus);

    Optional<StudyGroupMember> findByStudyGroupAndMember_Id(StudyGroup studyGroup, Long memberId);

}