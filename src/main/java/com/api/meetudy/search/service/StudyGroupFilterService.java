package com.api.meetudy.search.service;

import com.api.meetudy.group.dto.StudyGroupDto;
import com.api.meetudy.group.entity.StudyGroup;
import com.api.meetudy.group.enums.GroupMemberStatus;
import com.api.meetudy.group.enums.Location;
import com.api.meetudy.group.enums.StudyCategory;
import com.api.meetudy.group.mapper.StudyGroupMapper;
import com.api.meetudy.group.repository.GroupMemberRepository;
import com.api.meetudy.group.repository.GroupRepository;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.recommendation.service.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class StudyGroupFilterService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final StudyGroupMapper studyGroupMapper;
    private final RecommendationService recommendationService;

    @Transactional(readOnly = true)
    public List<StudyGroupDto> filterStudyGroups(String maxParticipantsCondition, Boolean isOnline,
                                                 StudyCategory category, Location location) {
        Specification<StudyGroup> spec = Specification.where(StudyGroupSpecification.isRecruitingSpecification());

        spec = spec.and(StudyGroupSpecification.maxParticipantsSpecification(maxParticipantsCondition));
        spec = spec.and(StudyGroupSpecification.isOnlineSpecification(isOnline));
        spec = spec.and(StudyGroupSpecification.categorySpecification(category));
        spec = spec.and(StudyGroupSpecification.locationSpecification(location));

        List<StudyGroup> filteredGroups = groupRepository.findAll(spec);
        return studyGroupMapper.toStudyGroupDtoList(filteredGroups);
    }

    @Transactional(readOnly = true)
    public List<StudyGroupDto> sortStudyGroups(String sortBy, Member member) {
        List<StudyGroup> groups = groupRepository.findAll();
        List<StudyGroupDto> result;

        switch (sortBy) {
            case "LATEST":
                groups.sort(Comparator.comparing(StudyGroup::getCreatedAt).reversed());
                result = studyGroupMapper.toStudyGroupDtoList(groups);
                break;
            case "OLDEST":
                groups.sort(Comparator.comparing(StudyGroup::getCreatedAt));
                result = studyGroupMapper.toStudyGroupDtoList(groups);
                break;
            case "POPULAR":
                groups.sort(Comparator.comparingInt(this::getJoinRequestCount).reversed());
                result = studyGroupMapper.toStudyGroupDtoList(groups);
                break;
            case "CLOSING_SOON":
                groups.sort(Comparator.comparingInt(this::getParticipantsDiff));
                result = studyGroupMapper.toStudyGroupDtoList(groups);
                break;
            case "RECOMMENDED":
                result = recommendationService.recommendStudyGroups(member);
                break;
            default:
                groups.sort(Comparator.comparing(StudyGroup::getCreatedAt).reversed());
                result = studyGroupMapper.toStudyGroupDtoList(groups);
                break;
        }

        return result;
    }

    private int getJoinRequestCount(StudyGroup studyGroup) {
        return (int) groupMemberRepository.countByStudyGroupAndStatus(studyGroup, GroupMemberStatus.REQUESTED);
    }

    private int getParticipantsDiff(StudyGroup studyGroup) {
        long currentMembersCount = groupMemberRepository.countByStudyGroupAndStatus(studyGroup, GroupMemberStatus.MEMBER);
        return Math.abs((int) (studyGroup.getMaxParticipants() - currentMembersCount));
    }

}