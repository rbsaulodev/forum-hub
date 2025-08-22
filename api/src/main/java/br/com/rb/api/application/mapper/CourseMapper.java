package br.com.rb.api.application.mapper;

import br.com.rb.api.application.dto.course.CourseDetailsDTO;
import br.com.rb.api.application.dto.course.CreateCourseDTO;
import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseMapper {

    public static Course toEntity(CreateCourseDTO dto, List<User> teachers) {
        Course course = new Course();
        course.setName(dto.name());
        course.setCategoryCourse(dto.categoryCourse());
        course.setTeachers(teachers);
        return course;
    }

    public CourseDetailsDTO toDetailsDTO(Course course) {
        return new CourseDetailsDTO(course);
    }
}