package com.kmp.mephi.studsys.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;


@Entity
@Table(name = "quiz_submissions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizSubmission extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;


    private Integer score;

    @Builder.Default
    private LocalDateTime takenAt = LocalDateTime.now();
}
