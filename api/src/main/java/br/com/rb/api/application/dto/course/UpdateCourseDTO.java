package br.com.rb.api.application.dto.course;

import br.com.rb.api.domain.model.CategoryCourse;
import br.com.rb.api.domain.model.Topic;
import br.com.rb.api.domain.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateCourseDTO (
        @NotBlank(message = "O nome do curso não pode estar em branco.")
        @Size(min = 3, max = 100, message = "O nome do curso deve ter entre 3 e 100 caracteres.")
        String name,

        @NotNull(message = "A categoria do curso é obrigatória.")
        CategoryCourse categoryCourse,

        @NotEmpty(message = "O curso deve ter pelo menos um professor.")
        @Size(max = 3, message = "O curso não pode ter mais de 3 professores.")
        List<User> teachers
){
}
