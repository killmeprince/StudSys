package com.kmp.mephi.studsys.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Profile extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


    @Column(length = 2000)
    private String bio;


    private String avatarUrl;
}
