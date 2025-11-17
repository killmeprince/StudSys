package com.kmp.mephi.studsys.service;

import com.kmp.mephi.studsys.entity.*;

import com.kmp.mephi.studsys.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepo;
    private final ModuleRepository moduleRepo;
    private final QuestionRepository questionRepo;
    private final QuizSubmissionRepository quizSubmissionRepo;
    private final UserRepository userRepo;

    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Transactional
    public Quiz create(Long moduleId, String title) {
        ModuleEntity module = moduleRepo.findById(moduleId).orElseThrow();
        Quiz quiz = Quiz.builder()
                .module(module)
                .title(title)
                .build();
        return quizRepo.save(quiz);
    }

    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Transactional
    public Question addQuestion(Long quizId, String text, QuestionType type) {
        Quiz quiz = quizRepo.findById(quizId).orElseThrow();
        Question q = Question.builder()
                .quiz(quiz)
                .text(text)
                .type(type)
                .build();
        return questionRepo.save(q);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @Transactional
    public QuizSubmission evaluateAndSubmit(Long quizId, Long studentId, int score) {
        Quiz quiz = quizRepo.findById(quizId).orElseThrow();
        User student = userRepo.findById(studentId).orElseThrow();

        QuizSubmission sub = QuizSubmission.builder()
                .quiz(quiz)
                .student(student)
                .score(score)
                .build();

        return quizSubmissionRepo.save(sub);
    }
}
