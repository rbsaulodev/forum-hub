package br.com.rb.api.application.dto.answer;

import jakarta.validation.constraints.Size;

public record UpdateAnswerDTO(
        @Size(min = 5, max = 500, message = "A resposta deve ter entre 5 e 500 caracteres.")
        String text
) {
}
