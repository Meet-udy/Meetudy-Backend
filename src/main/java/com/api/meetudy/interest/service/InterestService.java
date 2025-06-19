package com.api.meetudy.interest.service;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.group.enums.StudyCategory;
import com.api.meetudy.interest.entity.Interest;
import com.api.meetudy.interest.entity.MemberInterest;
import com.api.meetudy.interest.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;

    public void setMemberInterests(List<StudyCategory> studyCategories, Member member) {
        if (studyCategories != null) {
            for (StudyCategory studyCategory : studyCategories) {
                Interest interest = interestRepository.findByStudyCategory(studyCategory);
                if (interest != null) {
                    MemberInterest memberInterest = new MemberInterest(member, interest);
                    member.getMemberInterests().add(memberInterest);
                }
            }
        }
    }

}