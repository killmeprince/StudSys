package com.kmp.mephi.studsys.repository;

import com.kmp.mephi.studsys.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> { }
