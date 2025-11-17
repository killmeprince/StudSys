package com.kmp.mephi.studsys.repository;

import com.kmp.mephi.studsys.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> { }
