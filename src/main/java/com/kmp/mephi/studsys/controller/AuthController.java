package com.kmp.mephi.studsys.controller;

import com.kmp.mephi.studsys.dto.*;
import com.kmp.mephi.studsys.entity.Role;
import com.kmp.mephi.studsys.entity.User;
import com.kmp.mephi.studsys.repository.UserRepository;
import com.kmp.mephi.studsys.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        if (userRepo.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already in use");
        }
        Role role = parseRole(req.role());

        User u = User.builder()
                .name(req.name())
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .role(role)
                .build();

        u = userRepo.save(u);
        return new AuthResponse(jwt.issueAccess(u), jwt.issueRefresh(u));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        User u = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "bad credentials"));
        if (!encoder.matches(req.password(), u.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "bad credentials");
        }
        return new AuthResponse(jwt.issueAccess(u), jwt.issueRefresh(u));
    }

    private Role parseRole(String r) {
        if (r == null) return Role.STUDENT;
        try { return Role.valueOf(r.toUpperCase()); }
        catch (IllegalArgumentException e) { return Role.STUDENT; }
    }
}
