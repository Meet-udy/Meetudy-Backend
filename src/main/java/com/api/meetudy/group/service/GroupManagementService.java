package com.api.meetudy.group.service;

import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.global.utils.LeaderAccessValidator;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.group.dto.StudyGroupDto;
import com.api.meetudy.group.dto.StudyGroupMemberDto;
import com.api.meetudy.group.dto.StudyGroupUpdateDto;
import com.api.meetudy.group.entity.StudyGroup;
import com.api.meetudy.group.entity.StudyGroupMember;
import com.api.meetudy.group.enums.GroupMemberStatus;
import com.api.meetudy.group.mapper.StudyGroupMapper;
import com.api.meetudy.group.repository.GroupMemberRepository;
import com.api.meetudy.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupManagementService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final StudyGroupMapper studyGroupMapper;
    private final LeaderAccessValidator leaderAccessValidator;
    private final CacheManager cacheManager;

    @Transactional
    @CacheEvict(value = {"myStudyGroups"}, key = "#member.id")
    public String createStudyGroup(StudyGroupDto studyGroupDto, Member member) {
        StudyGroup studyGroup = studyGroupMapper.toStudyGroup(studyGroupDto, member);

        groupRepository.save(studyGroup);

        StudyGroupMember leaderMember = new StudyGroupMember(null, GroupMemberStatus.LEADER, member, studyGroup);
        groupMemberRepository.save(leaderMember);

        member.updateActivityScore(member.getActivityScore() + 8);

        return "Study group has been successfully created.";
    }

    @Transactional
    @CacheEvict(value = "studyGroupDetail", key = "#groupId")
    public String updateGroupInfo(Long groupId, StudyGroupUpdateDto groupUpdateDto, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        leaderAccessValidator.checkLeaderAccess(member, studyGroup);

        studyGroupMapper.updateStudyGroupFromDto(groupUpdateDto, studyGroup);

        return "Study group information has been updated.";
    }

    @Transactional
    public String approveJoinRequest(Long groupMemberId, Member member) {
        StudyGroupMember newMember = groupMemberRepository.findById(groupMemberId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_MEMBER_NOT_FOUND));

        StudyGroup studyGroup = newMember.getStudyGroup();

        leaderAccessValidator.checkLeaderAccess(member, studyGroup);

        if (studyGroup.getMembers().size() >= studyGroup.getMaxParticipants()) {
            throw new CustomException(ErrorStatus.MAX_PARTICIPANTS_EXCEEDED);
        }

        newMember.updateStatus(GroupMemberStatus.MEMBER);

        Member groupMember = newMember.getMember();
        groupMember.updateActivityScore(groupMember.getActivityScore() + 5);

        evictJoinRequestCaches(groupMember.getId(), studyGroup.getId());

        return "The join request has been approved.";
    }

    @Transactional
    public String rejectJoinRequest(Long groupMemberId, Member member) {
        StudyGroupMember groupMember = groupMemberRepository.findById(groupMemberId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_MEMBER_NOT_FOUND));

        StudyGroup studyGroup = groupMember.getStudyGroup();

        leaderAccessValidator.checkLeaderAccess(member, studyGroup);

        studyGroup.getMembers().remove(groupMember);
        evictJoinRequestCaches(groupMember.getMember().getId(), studyGroup.getId());

        return "The join request has been rejected.";
    }

    @Transactional
    @CacheEvict(value = "studyGroupDetail", key = "#groupId")
    public StudyGroupDto closeRecruitment(Long groupId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        leaderAccessValidator.checkLeaderAccess(member, studyGroup);

        studyGroup.closeRecruitment();
        groupRepository.save(studyGroup);

        return studyGroupMapper.toStudyGroupDto(studyGroup);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "groupMembers", key = "#groupId + '_' + #status")
    public List<StudyGroupMemberDto> getMembersByStatus(Long groupId, GroupMemberStatus status, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));
        leaderAccessValidator.checkLeaderAccess(member, studyGroup);

        List<StudyGroupMember> members = groupMemberRepository
                .findByStudyGroupIdAndStatus(groupId, status);

        return studyGroupMapper.toStudyGroupMemberDtoList(members);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "groupMembers", key = "#groupId"),
            @CacheEvict(value = "myStudyGroups", key = "#memberId")
    })
    public String removeMember(Long groupId, Long memberId, Member member) {
        StudyGroup studyGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));
        leaderAccessValidator.checkLeaderAccess(member, studyGroup);

        StudyGroupMember memberToRemove = groupMemberRepository.findByStudyGroupAndMember_Id(studyGroup, memberId)
                .orElseThrow(() -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND));

        studyGroup.getMembers().remove(memberToRemove);

        return "Member has been removed.";
    }

    private void evictJoinRequestCaches(Long memberId, Long groupId) {
        cacheManager.getCache("pendingStudyGroups").evict(memberId);
        cacheManager.getCache("myStudyGroups").evict(memberId);
        cacheManager.getCache("groupMembers").evict(groupId + "_REQUESTED");
    }

}