package com.kmp.mephi.studsys.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}
