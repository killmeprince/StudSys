package com.kmp.mephi.studsys.service;

import com.kmp.mephi.studsys.entity.User;
import com.kmp.mephi.studsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kmp.mephi.studsys.entity.Role;


@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User create(String name, String email, Role role) {
        User u = User.builder().name(name).email(email).role(role).build();
        return userRepository.save(u);
    }


    public User getOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }
}
