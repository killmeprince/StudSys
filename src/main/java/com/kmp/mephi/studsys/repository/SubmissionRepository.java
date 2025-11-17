package com.kmp.mephi.studsys.repository;

import com.kmp.mephi.studsys.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    boolean existsByAssignment_IdAndStudent_Id(Long assignmentId, Long studentId);
}
