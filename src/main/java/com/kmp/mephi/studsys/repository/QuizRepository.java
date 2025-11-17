package com.kmp.mephi.studsys.repository;

import com.kmp.mephi.studsys.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> { }
