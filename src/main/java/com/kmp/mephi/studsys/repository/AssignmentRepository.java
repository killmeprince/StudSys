package com.kmp.mephi.studsys.repository;

import com.kmp.mephi.studsys.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> { }
