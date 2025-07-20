package com.api.meetudy.group.service;

import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.group.dto.StudyGroupDto;
import com.api.meetudy.group.entity.StudyGroup;
import com.api.meetudy.group.entity.StudyGroupMember;
import com.api.meetudy.group.enums.GroupMemberStatus;
import com.api.meetudy.group.mapper.StudyGroupMapper;
import com.api.meetudy.group.repository.GroupMemberRepository;
import com.api.meetudy.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = "pendingStudyGroups", key = "#member.id")
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
    @Cacheable(value = "myStudyGroups", key = "#member.id")
    public List<StudyGroupDto> getAllStudyGroupsWithMyStatus(Member member) {
        List<GroupMemberStatus> statuses = List.of(GroupMemberStatus.LEADER, GroupMemberStatus.MEMBER, GroupMemberStatus.REQUESTED);
        List<StudyGroupMember> groupMembers = groupMemberRepository.findByMemberAndStatusIn(member, statuses);

        return studyGroupMapper.toStudyGroupDtoListFromMembers(groupMembers);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pendingStudyGroups", key = "#member.id")
    public List<StudyGroupDto> getPendingStudyGroups(Member member) {
        List<StudyGroupMember> pendingGroupMembers = groupMemberRepository.findByMemberAndStatus(member, GroupMemberStatus.REQUESTED);
        return studyGroupMapper.toStudyGroupDtoListFromMembers(pendingGroupMembers);
    }

    @Transactional(readOnly = true)
    public StudyGroupDto getStudyGroupById(Long groupId, Member currentUser) {
        StudyGroupDto studyGroupDto = getCachedStudyGroupDto(groupId);

        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        studyGroup.getMembers().stream()
                .filter(member -> member.getMember().getId().equals(currentUser.getId()))
                .findFirst()
                .ifPresent(member -> {
                    GroupMemberStatus status = member.getStatus();
                    studyGroupDto.setMyRole(status);
                });

        return studyGroupDto;
    }

    @Transactional
    @CacheEvict(value = "myStudyGroups", key = "#member.id")
    public String leaveStudyGroup(Long groupId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        StudyGroupMember memberToLeave = groupMemberRepository.findByStudyGroupAndMember_Id(studyGroup, member.getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND));

        studyGroup.getMembers().remove(memberToLeave);
        groupMemberRepository.delete(memberToLeave);

        return "Leave request submitted successfully.";
    }

    @Cacheable(value = "studyGroupDetail", key = "#groupId")
    public StudyGroupDto getCachedStudyGroupDto(Long groupId) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));
        return studyGroupMapper.toStudyGroupDto(studyGroup);
    }

}