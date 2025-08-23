package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.course.CourseDetailsDTO;
import br.com.rb.api.application.dto.course.CreateCourseDTO;
import br.com.rb.api.application.dto.course.UpdateCourseDTO;
import br.com.rb.api.application.mapper.CourseMapper;
import br.com.rb.api.application.exception.CourseHasTopicsException;
import br.com.rb.api.application.validations.CreateCourseValidator;
import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.Topic;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.CourseRepository;
import br.com.rb.api.domain.repository.TopicRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CreateCourseValidator createCourseValidator;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CreateCourseValidator createCourseValidator, UserRepository userRepository, TopicRepository topicRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.createCourseValidator = createCourseValidator;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.courseMapper = courseMapper;
    }

    public List<String> listAllStudentNames(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + id));
        return course.getEnrolledUsers().stream()
                .map(User::getName)
                .toList();
    }

    public List<String> listAllTeacherNames(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + id));

        return course.getTeachers().stream()
                .map(User::getName)
                .toList();
    }

    public List<String> listAllTopicTitles(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + id));

        return course.getTopics().stream()
                .map(Topic::getTitle)
                .toList();
    }

    public Page<CourseDetailsDTO> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .map(courseMapper::toDetailsDTO);
    }

    @Transactional
    public CourseDetailsDTO create(CreateCourseDTO dto) {
        createCourseValidator.validate(dto);
        List<User> teachers = userRepository.findAllById(dto.teacherIds());
        Course newCourse = CourseMapper.toEntity(dto, teachers);
        Course savedCourse = courseRepository.save(newCourse);
        return courseMapper.toDetailsDTO(savedCourse);
    }

    public CourseDetailsDTO findById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + id));
        return courseMapper.toDetailsDTO(course);
    }

    @Transactional
    public CourseDetailsDTO update(Long id, UpdateCourseDTO dto) {
        createCourseValidator.validate(new CreateCourseDTO(dto.name(), dto.categoryCourse(), dto.teacherIds()));
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + id));
        List<User> teachers = userRepository.findAllById(dto.teacherIds());

        course.setName(dto.name());
        course.setCategoryCourse(dto.categoryCourse());
        course.setTeachers(teachers);

        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toDetailsDTO(updatedCourse);
    }

    @Transactional
    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Curso não encontrado com o ID: " + id);
        }
        if (topicRepository.existsByCourseId(id)) {
            throw new CourseHasTopicsException("Não é possível excluir um curso que já possui tópicos associados.");
        }
        courseRepository.deleteById(id);
    }
}