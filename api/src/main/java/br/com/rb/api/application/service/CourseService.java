package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.course.CreateCourseDTO;
import br.com.rb.api.application.dto.course.UpdateCourseDTO;
import br.com.rb.api.application.mapper.CourseMapper;
import br.com.rb.api.application.validations.CourseHasTopicsException;
import br.com.rb.api.application.validations.CreateCourseValidator;
import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.CourseRepository;
import br.com.rb.api.domain.repository.TopicRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CreateCourseValidator createCourseValidator;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    public CourseService(CourseRepository courseRepository, CreateCourseValidator createCourseValidator, UserRepository userRepository, TopicRepository topicRepository) {
        this.courseRepository = courseRepository;
        this.createCourseValidator = createCourseValidator;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public Course create(CreateCourseDTO dto) {
        createCourseValidator.validate(dto);
        List<User> teachers = userRepository.findAllById(dto.teacherIds());

        Course newCourse = CourseMapper.toEntity(dto, teachers);
        return courseRepository.save(newCourse);
    }

    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + id));
    }


    @Transactional
    public Course update(Long id, UpdateCourseDTO dto) {
        createCourseValidator.validate(new CreateCourseDTO(dto.name(), dto.categoryCourse(), dto.teacherIds()));

        Course course = findById(id);
        List<User> teachers = userRepository.findAllById(dto.teacherIds());

        course.setName(dto.name());
        course.setCategoryCourse(dto.categoryCourse());
        course.setTeachers(teachers);

        return courseRepository.save(course);
    }

    @Transactional
    public void delete(Long id) {
        Course course = findById(id);

        if (topicRepository.existsByCourseId(id)) {
            throw new CourseHasTopicsException("Não é possível excluir um curso que já possui tópicos associados.");
        }

        courseRepository.delete(course);
    }

}
