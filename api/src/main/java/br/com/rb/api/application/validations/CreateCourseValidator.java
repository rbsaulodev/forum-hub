package br.com.rb.api.application.validations;

import br.com.rb.api.application.dto.course.CreateCourseDTO;
import br.com.rb.api.application.exception.TeacherSkillMismatchException;
import br.com.rb.api.domain.model.CategoryCourse;
import br.com.rb.api.domain.model.Role;
import br.com.rb.api.domain.model.SpecialtyTeacher;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateCourseValidator {
    private final UserRepository userRepository;

    public CreateCourseValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(CreateCourseDTO dto) {
        List<User> teachers = userRepository.findAllById(dto.teacherIds());

        if (teachers.size() != dto.teacherIds().size()) {
            throw new EntityNotFoundException("Um ou mais professores não foram encontrados.");
        }

        for (User teacher : teachers) {
            boolean isTeacher = teacher.getRoles().stream()
                    .map(Role::getName)
                    .anyMatch(name -> name.equals("ROLE_TEACHER"));
            if (!isTeacher) {
                throw new TeacherSkillMismatchException("O usuário com ID " + teacher.getId() + " não é um professor.");
            }

            if (!areSkillsCompatible(teacher, dto.categoryCourse())) {
                throw new TeacherSkillMismatchException(
                        "O professor " + teacher.getName() + " não possui as habilidades necessárias para o curso de " + dto.categoryCourse().getDisplayName()
                );
            }
        }
    }

    private boolean areSkillsCompatible(User teacher, CategoryCourse category) {
        if (teacher.getSpecialty() == null) {
            return false;
        }

        return switch (category) {
            case PROGRAMMING, DEVOPS -> teacher.getSpecialty() == SpecialtyTeacher.BACK_END ||
                    teacher.getSpecialty() == SpecialtyTeacher.DEVOPS ||
                    teacher.getSpecialty() == SpecialtyTeacher.FULL_STACK;
            case FRONT_END, UX_DESIGN -> teacher.getSpecialty() == SpecialtyTeacher.FRONT_END ||
                    teacher.getSpecialty() == SpecialtyTeacher.FULL_STACK;
            case DATA_SCIENCE -> teacher.getSpecialty() == SpecialtyTeacher.DATABASE ||
                    teacher.getSpecialty() == SpecialtyTeacher.BACK_END;
            case MOBILE -> true;
            default -> false;
        };
    }
}
