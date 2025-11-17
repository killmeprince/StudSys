package com.kmp.mephi.studsys.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateCourseRequest(
        @NotBlank String title,
        String description,
        @NotNull Long categoryId,
        @NotNull Long teacherId
) {}
