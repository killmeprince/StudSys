package com.kmp.mephi.studsys.repository;

import com.kmp.mephi.studsys.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> { }
