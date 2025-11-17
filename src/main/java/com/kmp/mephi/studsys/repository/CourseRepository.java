package com.kmp.mephi.studsys.repository;

import com.kmp.mephi.studsys.entity.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByTitle(String title);


    @EntityGraph(attributePaths = {"category","teacher"})
    List<Course> findAllByCategory_Name(String categoryName);


    @Query("select c from Course c left join fetch c.modules m left join fetch m.lessons where c.id = :id")
    Optional<Course> fetchGraphById(Long id);


}

