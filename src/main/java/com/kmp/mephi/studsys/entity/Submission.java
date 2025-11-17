package com.kmp.mephi.studsys.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;


@Entity
@Table(name = "submissions", uniqueConstraints = @UniqueConstraint(columnNames = {"assignment_id","student_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Submission extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Builder.Default
    private LocalDateTime submittedAt = LocalDateTime.now();



    @Column(length = 10000)
    private String content;


    private Integer score;


    @Column(length = 2000)
    private String feedback;
}
