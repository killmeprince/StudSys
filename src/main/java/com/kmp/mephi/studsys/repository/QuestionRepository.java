package com.kmp.mephi.studsys.repository;

import com.kmp.mephi.studsys.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> { }
