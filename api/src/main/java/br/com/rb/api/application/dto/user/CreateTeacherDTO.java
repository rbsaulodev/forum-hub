package br.com.rb.api.application.dto.user;

import br.com.rb.api.domain.model.SpecialtyTeacher;
import br.com.rb.api.domain.model.StackTeacher;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTeacherDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
        String name,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O formato do email é inválido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String password,

        @NotNull(message = "A especialidade é obrigatória.")
        SpecialtyTeacher specialty,

        @NotNull(message = "A stack é obrigatória.")
        StackTeacher stack
) {}