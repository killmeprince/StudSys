package com.kmp.mephi.studsys.controller;

import com.kmp.mephi.studsys.dto.EnrollRequest;
import com.kmp.mephi.studsys.entity.Enrollment;
import com.kmp.mephi.studsys.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enrollment enroll(@Valid @RequestBody EnrollRequest req) {
        return enrollmentService.enroll(req.userId(), req.courseId());
    }
}
