package br.com.rb.api.application.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTopicDTO(
        @NotBlank(message = "O título não pode estar em branco.")
        @Size(min = 5, max = 255, message = "O título deve ter entre 5 e 255 caracteres.")
        String title,

        @NotBlank(message = "O texto do tópico não pode estar em branco.")
        @Size(min = 10, message = "O texto do tópico deve ter no mínimo 10 caracteres.")
        String text
) {
}