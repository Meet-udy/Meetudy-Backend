package com.api.meetudy.study.group.service;

import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.dto.StudyGroupDto;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.enums.GroupMemberStatus;
import com.api.meetudy.study.group.mapper.StudyGroupMapper;
import com.api.meetudy.study.group.repository.GroupMemberRepository;
import com.api.meetudy.study.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final StudyGroupMapper studyGroupMapper;

    @Transactional
    public String requestJoinGroup(Long groupId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        boolean isAlreadyRequested = groupMemberRepository.existsByStudyGroupAndMember(studyGroup, member);
        if (isAlreadyRequested) {
            throw new CustomException(ErrorStatus.REQUEST_ALREADY_SENT);
        }

        StudyGroupMember studyGroupMember = studyGroupMapper.toStudyGroupMember(studyGroup, member);
        groupMemberRepository.save(studyGroupMember);

        return "Join request submitted successfully";
    }

    @Transactional(readOnly = true)
    public List<StudyGroupDto> getCreatedStudyGroups(Member member) {
        List<StudyGroupMember> groupMembers = groupMemberRepository.findByMemberAndStatus(member, GroupMemberStatus.LEADER);
        return studyGroupMapper.toStudyGroupDtoListFromMembers(groupMembers);
    }

    @Transactional(readOnly = true)
    public List<StudyGroupDto> getJoinedStudyGroups(Member member) {
        List<StudyGroupMember> groupMembers = groupMemberRepository.findByMemberAndStatus(member, GroupMemberStatus.MEMBER);
        return studyGroupMapper.toStudyGroupDtoListFromMembers(groupMembers);
    }

    @Transactional(readOnly = true)
    public List<StudyGroupDto> getPendingStudyGroups(Member member) {
        List<StudyGroupMember> pendingGroupMembers = groupMemberRepository.findByMemberAndStatus(member, GroupMemberStatus.REQUESTED);
        return studyGroupMapper.toStudyGroupDtoListFromMembers(pendingGroupMembers);
    }

    @Transactional(readOnly = true)
    public StudyGroupDto getStudyGroupById(Long groupId) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        return studyGroupMapper.toStudyGroupDto(studyGroup);
    }

    @Transactional
    public String leaveStudyGroup(Long groupId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        StudyGroupMember memberToLeave = groupMemberRepository.findByStudyGroupAndMember_Id(studyGroup, member.getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND));

        studyGroup.getMembers().remove(memberToLeave);
        groupMemberRepository.delete(memberToLeave);

        return "Leave request submitted successfully.";
    }

}