package com.kmp.mephi.studsys.mapper;

import com.kmp.mephi.studsys.dto.UserDto;
import com.kmp.mephi.studsys.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    UserDto toDto(User user);
}
