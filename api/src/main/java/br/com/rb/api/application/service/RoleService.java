package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.role.CreateRoleDTO;
import br.com.rb.api.application.mapper.RoleMapper;
import br.com.rb.api.application.validations.EntityAlreadyExistsException;
import br.com.rb.api.domain.model.Role;
import br.com.rb.api.domain.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Role create(CreateRoleDTO dto){
        if (roleRepository.existsByName(dto.name())) {
            throw new EntityAlreadyExistsException("Já existe uma role com este nome!");
        }
        Role role = RoleMapper.toEntity(dto);
        return roleRepository.save(role);
    }
}
