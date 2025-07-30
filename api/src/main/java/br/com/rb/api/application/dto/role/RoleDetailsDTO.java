package br.com.rb.api.application.dto.role;

import br.com.rb.api.domain.model.Role;

public record RoleDetailsDTO(
        String name
) {

    public RoleDetailsDTO(Role role) {
        this(role.getName());
    }
}
