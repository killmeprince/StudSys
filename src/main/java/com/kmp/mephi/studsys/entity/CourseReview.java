package com.kmp.mephi.studsys.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;


@Entity
@Table(name = "course_reviews")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CourseReview extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;


    @Column(nullable = false)
    private int rating;


    @Column(length = 2000)
    private String comment;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAtReview = LocalDateTime.now();
}