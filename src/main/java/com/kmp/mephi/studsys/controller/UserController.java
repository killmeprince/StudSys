package com.kmp.mephi.studsys.controller;

import com.kmp.mephi.studsys.mapper.UserMapper;
import com.kmp.mephi.studsys.service.UserService;
import com.kmp.mephi.studsys.dto.CreateUserRequest;
import com.kmp.mephi.studsys.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody CreateUserRequest req) {
        return userMapper.toDto(userService.create(req.name(), req.email(), req.role()));
    }
}
