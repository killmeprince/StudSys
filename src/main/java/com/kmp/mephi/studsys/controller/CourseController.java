package com.kmp.mephi.studsys.controller;
import com.kmp.mephi.studsys.dto.CourseDto;
import com.kmp.mephi.studsys.dto.CreateCourseRequest;
import com.kmp.mephi.studsys.mapper.CourseMapper;
import com.kmp.mephi.studsys.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto create(@Valid @RequestBody CreateCourseRequest req) {
        return courseMapper.toDto(courseService.createCourse(req.title(), req.description(), req.categoryId(), req.teacherId()));
    }


    @GetMapping("/{id}")
    public CourseDto get(@PathVariable Long id) {
        return courseMapper.toDto(courseService.getCourseGraph(id));
    }


    @GetMapping
    public List<CourseDto> byCategory(@RequestParam(required = false) String category) {
        return courseService.byCategory(Objects.requireNonNullElse(category, "")).stream().map(courseMapper::toDto).toList();
    }
}