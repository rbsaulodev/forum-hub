package br.com.rb.api.application.dto.user;

import jakarta.validation.constraints.Email;

import java.util.Set;

public record UpdateUserDTO(
        String name,

        @Email(message = "Formato de email inválido.")
        String email,
        String password,
        Set<String> roles
) {
}