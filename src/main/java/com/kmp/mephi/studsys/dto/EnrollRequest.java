package com.kmp.mephi.studsys.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollRequest(@NotNull Long userId, @NotNull Long courseId) {}
