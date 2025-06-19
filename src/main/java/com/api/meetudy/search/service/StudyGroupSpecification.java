package com.api.meetudy.search.service;

import com.api.meetudy.group.entity.StudyGroup;
import com.api.meetudy.group.enums.Location;
import com.api.meetudy.group.enums.StudyCategory;
import com.api.meetudy.search.utils.StudyCategoryConverter;
import org.springframework.data.jpa.domain.Specification;

public class StudyGroupSpecification {

    public static Specification<StudyGroup> searchKeywordSpecification(String searchKeyword) {
        return (root, query, criteriaBuilder) -> {
            String keyword = "%" + searchKeyword + "%";
            StudyCategory category = StudyCategoryConverter.fromKoreanToEnum(searchKeyword);

            if (category != null) {
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("name"), keyword),
                        criteriaBuilder.like(root.get("description"), keyword),
                        criteriaBuilder.equal(root.get("category"), category)
                );
            } else {
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("name"), keyword),
                        criteriaBuilder.like(root.get("description"), keyword)
                );
            }
        };
    }

    public static Specification<StudyGroup> maxParticipantsSpecification(String maxParticipantsCondition) {
        return (root, query, criteriaBuilder) -> {
            if (maxParticipantsCondition != null) {
                switch (maxParticipantsCondition) {
                    case "3명 이하":
                        return criteriaBuilder.lessThanOrEqualTo(root.get("maxParticipants"), 3);
                    case "5명 이하":
                        return criteriaBuilder.lessThanOrEqualTo(root.get("maxParticipants"), 5);
                    case "7명 이하":
                        return criteriaBuilder.lessThanOrEqualTo(root.get("maxParticipants"), 7);
                    case "8명 이상":
                        return criteriaBuilder.greaterThanOrEqualTo(root.get("maxParticipants"), 8);
                    default:
                        return criteriaBuilder.conjunction();
                }
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<StudyGroup> isOnlineSpecification(Boolean isOnline) {
        return (root, query, criteriaBuilder) -> {
            if(isOnline != null) {
                return criteriaBuilder.equal(root.get("isOnline"), isOnline);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<StudyGroup> categorySpecification(StudyCategory category) {
        return (root, query, criteriaBuilder) -> {
            if(category != null) {
                return criteriaBuilder.equal(root.get("category"), category);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<StudyGroup> locationSpecification(Location location) {
        return (root, query, criteriaBuilder) -> {
            if(location != null) {
                return criteriaBuilder.equal(root.get("location"), location);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<StudyGroup> isRecruitingSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isRecruiting"));
    }

}