package com.kmp.mephi.studsys.service;

import com.kmp.mephi.studsys.entity.*;
import com.kmp.mephi.studsys.repository.*;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final ModuleRepository moduleRepo;
    private final LessonRepository lessonRepo;

    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Transactional
    public Course createCourse(String title, String description, Long categoryId, Long teacherId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + teacherId));


        Course c = Course.builder()
                .title(title)
                .description(description)
                .category(category)
                .teacher(teacher)
                .build();
        return courseRepo.save(c);
    }


    public Course getCourseGraph(Long id) {
        return courseRepo.fetchGraphById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
    }

    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Transactional
    public ModuleEntity addModule(Long courseId, String title, int order) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        ModuleEntity m = ModuleEntity.builder().course(course).title(title).orderIndex(order).build();
        return moduleRepo.save(m);
    }

    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Transactional
    public Lesson addLesson(Long moduleId, String title, String content) {
        ModuleEntity m = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Module not found: " + moduleId));
        Lesson lesson = Lesson.builder().module(m).title(title).content(content).build();
        return lessonRepo.save(lesson);
    }


    public List<Course> byCategory(String category) { return courseRepo.findAllByCategory_Name(category); }
}
