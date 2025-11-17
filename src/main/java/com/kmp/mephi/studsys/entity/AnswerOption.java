package com.kmp.mephi.studsys.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "answer_options")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AnswerOption extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;


    @Column(nullable = false)
    private String text;


    @Column(nullable = false)
    private boolean isCorrect;
}