package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.course.CourseDetailsDTO;
import br.com.rb.api.application.exception.EntityAlreadyExistsException;
import br.com.rb.api.application.mapper.CourseMapper;
import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.CourseRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public StudentService(UserRepository userRepository, CourseRepository courseRepository, CourseMapper courseMapper) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Transactional
    public CourseDetailsDTO enroll(long userId, long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + courseId));

        boolean isTeacherOfThisCourse = course.getTeachers().contains(user);
        if (isTeacherOfThisCourse) {
            throw new IllegalArgumentException("Você não pode se matricular em um curso que você leciona.");
        }

        boolean isAlreadyEnrolled = course.getEnrolledUsers().contains(user);
        if (isAlreadyEnrolled) {
            throw new EntityAlreadyExistsException("Usuário já matriculado neste curso.");
        }

        course.getEnrolledUsers().add(user);
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toDetailsDTO(updatedCourse);
    }

    @Transactional
    public CourseDetailsDTO unenroll(long idStudent, long idCourse){
        User student = userRepository.findById(idStudent)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + idStudent));

        Course course = courseRepository.findById(idCourse)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + idCourse));

        course.getEnrolledUsers().remove(student);
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toDetailsDTO(updatedCourse);
    }
}