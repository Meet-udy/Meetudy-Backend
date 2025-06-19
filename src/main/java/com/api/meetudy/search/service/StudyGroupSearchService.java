package com.api.meetudy.search.service;

import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.group.dto.StudyGroupDto;
import com.api.meetudy.group.entity.StudyGroup;
import com.api.meetudy.group.enums.StudyCategory;
import com.api.meetudy.group.mapper.StudyGroupMapper;
import com.api.meetudy.group.repository.GroupRepository;
import com.api.meetudy.search.dto.AutoCompleteDto;
import com.api.meetudy.search.utils.StudyCategoryConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyGroupSearchService {

    private final GroupRepository groupRepository;
    private final StudyGroupMapper studyGroupMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String REDIS_AUTO_COMPLETE_PREFIX = "autocomplete::";

    /*@Transactional(readOnly = true)
    public List<StudyGroupDto> searchStudyGroups(String searchKeyword) {
        Specification<StudyGroup> spec = Specification.where(StudyGroupSpecification.isRecruitingSpecification());

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            spec = spec.and(StudyGroupSpecification.searchKeywordSpecification(searchKeyword));
        }

        List<StudyGroup> searchResults = groupRepository.findAll(spec);
        return studyGroupMapper.toStudyGroupDtoList(searchResults);
    }*/

    public AutoCompleteDto getAutoCompleteSuggestions(String query) {
        String key = REDIS_AUTO_COMPLETE_PREFIX + query;
        Object cachedRaw = redisTemplate.opsForValue().get(key);

        if (cachedRaw != null) {
            return objectMapper.convertValue(cachedRaw, AutoCompleteDto.class);
        }

        List<String> matchedCategories = StudyCategoryConverter.getAllKoreanCategoriesExcludingOthers().stream()
                .filter(kor -> kor.startsWith(query))
                .collect(Collectors.toList());

        List<String> matchedGroupNames = groupRepository.findByNameContainingIgnoreCase(query).stream()
                .map(StudyGroup::getName)
                .collect(Collectors.toList());

        AutoCompleteDto result = new AutoCompleteDto(matchedCategories, matchedGroupNames);
        redisTemplate.opsForValue().set(key, result, Duration.ofHours(1));
        return result;
    }

    public List<StudyGroupDto> searchByCategory(String koreanCategory) {
        StudyCategory category = StudyCategoryConverter.fromKoreanToEnum(koreanCategory);
        if (category == null || category == StudyCategory.OTHERS) {
            throw new CustomException(ErrorStatus.INVALID_STUDY_CATEGORY);
        }
        List<StudyGroup> studyGroups = groupRepository.findByCategory(category);
        return studyGroupMapper.toStudyGroupDtoList(studyGroups);
    }

    public StudyGroupDto searchByExactName(String groupName) {
        StudyGroup studyGroup = groupRepository.findByNameContainingIgnoreCase(groupName).stream()
                .filter(g -> g.getName().equalsIgnoreCase(groupName))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorStatus.GROUP_NOT_FOUND));

        return studyGroupMapper.toStudyGroupDto(studyGroup);
    }

}