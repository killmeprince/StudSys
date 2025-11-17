package com.kmp.mephi.studsys.mapper;

import com.kmp.mephi.studsys.dto.CourseDto;
import com.kmp.mephi.studsys.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CourseMapper {
    @Mapping(target="teacherId", source="teacher.id")
    @Mapping(target = "category", source = "category.name")
    CourseDto toDto(Course c);
}
