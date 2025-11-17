package com.kmp.mephi.studsys.repository;

import com.kmp.mephi.studsys.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudent_IdAndCourse_Id(Long studentId, Long courseId);
}
