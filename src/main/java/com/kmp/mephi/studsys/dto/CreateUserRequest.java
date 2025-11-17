package com.kmp.mephi.studsys.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.kmp.mephi.studsys.entity.Role;

public record CreateUserRequest(
@NotBlank String name,
@Email String email,
@NotNull Role role
) {}