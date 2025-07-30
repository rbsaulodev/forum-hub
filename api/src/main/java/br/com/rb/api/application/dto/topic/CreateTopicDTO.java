package br.com.rb.api.application.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateTopicDTO(
        @NotBlank(message = "O título não pode estar em branco.")
        @Size(min = 5, max = 255, message = "O título deve ter entre 5 e 255 caracteres.")
        String title,

        @NotBlank(message = "O texto do tópico não pode estar em branco.")
        @Size(min = 10, message = "O texto do tópico deve ter no mínimo 10 caracteres.")
        String text,

        @NotNull(message = "O ID do autor é obrigatório.")
        @Positive(message = "O ID do autor deve ser um número positivo.")
        Long authorId,

        @NotNull(message = "O ID do curso é obrigatório.")
        @Positive(message = "O ID do curso deve ser um número positivo.")
        Long courseId
) {}