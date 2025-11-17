package com.kmp.mephi.studsys.repository;

import com.kmp.mephi.studsys.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> { }
