package com.api.meetudy.interest.repository;

import com.api.meetudy.group.enums.StudyCategory;
import com.api.meetudy.interest.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    Interest findByStudyCategory(StudyCategory studyCategory);

}