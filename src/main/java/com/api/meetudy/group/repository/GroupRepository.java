package com.api.meetudy.group.repository;

import com.api.meetudy.group.entity.StudyGroup;
import com.api.meetudy.group.enums.StudyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<StudyGroup, Long>, JpaSpecificationExecutor<StudyGroup> {

    List<StudyGroup> findByNameContainingIgnoreCase(String keyword);

    List<StudyGroup> findByCategory(StudyCategory category);

}