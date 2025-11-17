package com.kmp.mephi.studsys.service;

import com.kmp.mephi.studsys.entity.Course;
import com.kmp.mephi.studsys.entity.Enrollment;
import com.kmp.mephi.studsys.entity.EnrollmentStatus;
import com.kmp.mephi.studsys.entity.User;
import com.kmp.mephi.studsys.repository.CourseRepository;
import com.kmp.mephi.studsys.repository.EnrollmentRepository;
import com.kmp.mephi.studsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepo;
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;

    @PreAuthorize("hasRole('STUDENT')")
    @Transactional
    public Enrollment enroll(Long userId, Long courseId) {
        if (enrollmentRepo.existsByStudent_IdAndCourse_Id(userId, courseId)) {
            throw new IllegalStateException("Already enrolled");
        }
        User student = userRepo.findById(userId).orElseThrow();
        Course course = courseRepo.findById(courseId).orElseThrow();
        Enrollment e = Enrollment.builder().student(student).course(course).status(EnrollmentStatus.ACTIVE).build();
        return enrollmentRepo.save(e);
    }
}
