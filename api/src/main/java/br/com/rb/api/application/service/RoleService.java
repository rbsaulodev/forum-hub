package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.role.CreateRoleDTO;
import br.com.rb.api.application.dto.role.UpdateRoleDTO;
import br.com.rb.api.application.mapper.RoleMapper;
import br.com.rb.api.application.validations.EntityAlreadyExistsException;
import br.com.rb.api.application.validations.RoleInUseException;
import br.com.rb.api.domain.model.Role;
import br.com.rb.api.domain.repository.RoleRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Role create(CreateRoleDTO dto){
        if (roleRepository.existsByName(dto.name())) {
            throw new EntityAlreadyExistsException("Já existe uma role com este nome!");
        }
        Role role = RoleMapper.toEntity(dto);
        return roleRepository.save(role);
    }

    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada com o ID: " + id));
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role update(Long id, UpdateRoleDTO dto) {
        Role role = findById(id);
        if (roleRepository.existsByName(dto.name()) && !role.getName().equalsIgnoreCase(dto.name())) {
            throw new EntityAlreadyExistsException("Já existe outra role com o nome " + dto.name());
        }
        role.setName(dto.name());
        return roleRepository.save(role);
    }

    @Transactional
    public void delete(Long id) {
        Role role = findById(id);

        if (userRepository.existsByRoles_Id(id)) {
            throw new RoleInUseException("Não é possível excluir um papel que está atribuído a um ou mais usuários.");
        }

        roleRepository.delete(role);
    }
}
