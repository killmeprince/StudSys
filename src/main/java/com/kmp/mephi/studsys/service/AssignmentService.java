package com.kmp.mephi.studsys.service;

import com.kmp.mephi.studsys.entity.Assignment;
import com.kmp.mephi.studsys.entity.Lesson;
import com.kmp.mephi.studsys.entity.Submission;
import com.kmp.mephi.studsys.entity.User;
import com.kmp.mephi.studsys.repository.AssignmentRepository;
import com.kmp.mephi.studsys.repository.LessonRepository;
import com.kmp.mephi.studsys.repository.SubmissionRepository;
import com.kmp.mephi.studsys.repository.UserRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssignmentService {
    private final AssignmentRepository assignmentRepo;
    private final LessonRepository lessonRepo;
    private final SubmissionRepository submissionRepo;
    private final UserRepository userRepo;

    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Transactional
    public Assignment create(Long lessonId, String title, String description) {
        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow();
        Assignment a = Assignment.builder().lesson(lesson).title(title).description(description).maxScore(100).build();
        return assignmentRepo.save(a);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @Transactional
    public Submission submit(Long assignmentId, Long studentId, String content) {
        Assignment a = assignmentRepo.findById(assignmentId).orElseThrow();
        User student = userRepo.findById(studentId).orElseThrow();

         if (submissionRepo.existsByAssignment_IdAndStudent_Id(assignmentId, studentId)) {
             throw new IllegalStateException("Submission already exists");
         }

        Submission s = Submission.builder()
                .assignment(a)
                .student(student)
                .content(content)
                .build();

        return submissionRepo.save(s);
    }
}
