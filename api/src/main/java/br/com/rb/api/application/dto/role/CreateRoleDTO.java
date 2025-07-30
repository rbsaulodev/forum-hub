package br.com.rb.api.application.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateRoleDTO(
        @NotBlank(message = "O nome do papel não pode estar em branco.")
        @Size(min = 5, message = "O nome do papel deve ter no mínimo 5 caracteres.")
        @Pattern(regexp = "^ROLE_[A-Z_]+$", message = "O nome do papel deve começar com 'ROLE_' e conter apenas letras maiúsculas e underscores.")
        String name
) {
}
