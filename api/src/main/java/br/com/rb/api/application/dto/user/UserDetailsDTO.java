package br.com.rb.api.application.dto.user;

import br.com.rb.api.domain.model.Role;
import br.com.rb.api.domain.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserDetailsDTO(
        Long id,
        String name,
        String email,
        Set<String> roles
) {
    public UserDetailsDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }
}