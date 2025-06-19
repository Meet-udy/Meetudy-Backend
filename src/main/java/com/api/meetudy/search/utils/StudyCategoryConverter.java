package com.api.meetudy.search.utils;

import com.api.meetudy.group.enums.StudyCategory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StudyCategoryConverter {

    public static StudyCategory fromKoreanToEnum(String koreanCategory) {
        switch (koreanCategory) {
            case "어학":
                return StudyCategory.LANGUAGE;
            case "자격증":
                return StudyCategory.CERTIFICATION;
            case "인문학":
                return StudyCategory.HUMANITIES;
            case "사회과학":
                return StudyCategory.SOCIAL_SCIENCES;
            case "디자인":
                return StudyCategory.DESIGN;
            case "과학":
                return StudyCategory.SCIENCE;
            case "프로그래밍":
                return StudyCategory.PROGRAMMING;
            case "취업/커리어":
                return StudyCategory.CAREER;
            case "고시/공무원":
                return StudyCategory.QUALIFICATION_EXAM;
            case "취미":
                return StudyCategory.HOBBY;
            case "기타":
                return StudyCategory.OTHERS;
            default:
                return null;
        }
    }

    public static String fromEnumToKorean(StudyCategory category) {
        switch (category) {
            case LANGUAGE:
                return "어학";
            case CERTIFICATION:
                return "자격증";
            case HUMANITIES:
                return "인문학";
            case SOCIAL_SCIENCES:
                return "사회과학";
            case DESIGN:
                return "디자인";
            case SCIENCE:
                return "과학";
            case PROGRAMMING:
                return "프로그래밍";
            case CAREER:
                return "취업/커리어";
            case QUALIFICATION_EXAM:
                return "고시/공무원";
            case HOBBY:
                return "취미";
            default:
                return null;
        }
    }

    public static List<String> getAllKoreanCategoriesExcludingOthers() {
        return Arrays.stream(StudyCategory.values())
                .filter(cat -> cat != StudyCategory.OTHERS)
                .map(StudyCategoryConverter::fromEnumToKorean)
                .collect(Collectors.toList());
    }

}