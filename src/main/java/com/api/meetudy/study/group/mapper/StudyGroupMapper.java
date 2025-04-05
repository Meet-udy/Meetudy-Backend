package com.api.meetudy.study.group.mapper;

import com.api.meetudy.interest.entity.Interest;
import com.api.meetudy.interest.entity.MemberInterest;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.dto.StudyGroupDto;
import com.api.meetudy.study.group.dto.StudyGroupMemberDto;
import com.api.meetudy.study.group.dto.StudyGroupUpdateDto;
import com.api.meetudy.study.group.entity.StudyGroup;
import com.api.meetudy.study.group.entity.StudyGroupMember;
import com.api.meetudy.study.group.enums.StudyCategory;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StudyGroupMapper {

    @Mapping(target = "id", ignore = true)
    StudyGroupMember toStudyGroupMember(StudyGroup studyGroup, Member member);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isOnline", source = "dto.isOnline")
    @Mapping(target = "location", source = "dto.location")
    @Mapping(target = "members", ignore = true)
    StudyGroup toStudyGroup(StudyGroupDto dto, Member leader);

    @Mapping(source = "member.nickname", target = "nickname")
    @Mapping(source = "member.major", target = "major")
    @Mapping(source = "member.introduction", target = "introduction")
    @Mapping(source = "member.activityScore", target = "activityScore")
    @Mapping(source = "member", target = "interests")
    StudyGroupMemberDto toStudyGroupMemberDto(StudyGroupMember studyGroupMember);

    StudyGroupDto toStudyGroupDto(StudyGroup studyGroup);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStudyGroupFromDto(StudyGroupUpdateDto dto, @MappingTarget StudyGroup studyGroup);

    @IterableMapping(elementTargetType = StudyGroupDto.class)
    List<StudyGroupDto> toStudyGroupDtoList(List<StudyGroup> studyGroups);

    @IterableMapping(elementTargetType = StudyGroupMemberDto.class)
    List<StudyGroupMemberDto> toStudyGroupMemberDtoList(List<StudyGroupMember> studyGroupMembers);

    default List<StudyGroupDto> toStudyGroupDtoListFromMembers(List<StudyGroupMember> studyGroupMembers) {
        return studyGroupMembers.stream()
                .map(StudyGroupMember::getStudyGroup)
                .map(this::toStudyGroupDto)
                .collect(Collectors.toList());
    }

    default List<StudyCategory> mapInterests(Member member) {
        if (member.getMemberInterests() == null) return Collections.emptyList();
        return member.getMemberInterests().stream()
                .map(MemberInterest::getInterest)
                .map(Interest::getStudyCategory)
                .collect(Collectors.toList());
    }

}