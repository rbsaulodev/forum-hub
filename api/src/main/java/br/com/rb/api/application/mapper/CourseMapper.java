package br.com.rb.api.application.mapper;

import br.com.rb.api.application.dto.course.CreateCourseDTO;
import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.User;
import java.util.List;

public class CourseMapper {

    public static Course toEntity(CreateCourseDTO dto, List<User> teachers) {
        Course course = new Course();
        course.setName(dto.name());
        course.setCategoryCourse(dto.categoryCourse());
        course.setTeachers(teachers);
        return course;
    }
}