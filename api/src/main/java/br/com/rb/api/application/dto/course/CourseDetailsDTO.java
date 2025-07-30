package br.com.rb.api.application.dto.course;

import br.com.rb.api.domain.model.CategoryCourse;
import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.User;

import java.util.List;
import java.util.stream.Collectors;

public record CourseDetailsDTO(
        Long id,
        String name,
        CategoryCourse categoryCourse,
        List<String> teacherNames
) {
    public CourseDetailsDTO(Course course) {
        this(
                course.getId(),
                course.getName(),
                course.getCategoryCourse(),
                course.getTeachers().stream().map(User::getName).collect(Collectors.toList())
        );
    }
}