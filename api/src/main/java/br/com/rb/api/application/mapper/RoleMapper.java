package br.com.rb.api.application.mapper;

import br.com.rb.api.application.dto.role.CreateRoleDTO;
import br.com.rb.api.domain.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public static Role toEntity(CreateRoleDTO dto) {
        Role role = new Role();
        role.setName(dto.name());
        return role;
    }
}
