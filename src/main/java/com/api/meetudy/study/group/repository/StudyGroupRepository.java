package com.api.meetudy.study.group.repository;

import com.api.meetudy.member.entity.Member;
import com.api.meetudy.study.group.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    List<StudyGroup> findByLeader(Member member);

}