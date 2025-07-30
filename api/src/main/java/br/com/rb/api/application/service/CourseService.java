package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.course.CreateCourseDTO;
import br.com.rb.api.application.mapper.CourseMapper;
import br.com.rb.api.application.validations.CreateCourseValidator;
import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.CourseRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CreateCourseValidator createCourseValidator;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, CreateCourseValidator createCourseValidator, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.createCourseValidator = createCourseValidator;
        this.userRepository = userRepository;
    }

    @Transactional
    public Course create(CreateCourseDTO dto) {
        createCourseValidator.validate(dto);
        List<User> teachers = userRepository.findAllById(dto.teacherIds());

        Course newCourse = CourseMapper.toEntity(dto, teachers);
        return courseRepository.save(newCourse);
    }

}
