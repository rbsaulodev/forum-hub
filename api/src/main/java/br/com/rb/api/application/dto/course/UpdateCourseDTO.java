package br.com.rb.api.application.dto.course;

import br.com.rb.api.domain.model.CategoryCourse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateCourseDTO(
        @Size(min = 3, max = 100, message = "O nome do curso deve ter entre 3 e 100 caracteres.")
        String name,

        CategoryCourse categoryCourse,

        @NotEmpty(message = "O curso deve ter pelo menos um professor.")
        @Size(max = 3, message = "O curso não pode ter mais de 3 professores.")
        List<Long> teacherIds
) {
}