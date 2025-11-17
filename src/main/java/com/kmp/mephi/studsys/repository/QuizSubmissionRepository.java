package com.kmp.mephi.studsys.repository;

import com.kmp.mephi.studsys.entity.QuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
}
