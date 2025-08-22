package br.com.rb.api.application.dto.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAnswerDTO(
        @NotBlank
        @Size(min = 5, max = 500, message = "A resposta deve ter entre 5 e 500 caracteres.")
        String text,

        @NotNull
        Long topicId
) { }