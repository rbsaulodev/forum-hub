package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.role.CreateRoleDTO;
import br.com.rb.api.application.dto.role.RoleDetailsDTO;
import br.com.rb.api.application.dto.role.UpdateRoleDTO;
import br.com.rb.api.application.mapper.RoleMapper;
import br.com.rb.api.application.exception.EntityAlreadyExistsException;
import br.com.rb.api.application.exception.RoleInUseException;
import br.com.rb.api.domain.model.Role;
import br.com.rb.api.domain.repository.RoleRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public RoleDetailsDTO create(CreateRoleDTO dto){
        if (roleRepository.existsByName(dto.name())) {
            throw new EntityAlreadyExistsException("Já existe uma role com este nome!");
        }
        Role role = roleMapper.toEntity(dto);
        Role savedRole = roleRepository.save(role);
        return new RoleDetailsDTO(savedRole);
    }

    public RoleDetailsDTO findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada com o ID: " + id));
        return new RoleDetailsDTO(role);
    }

    public List<RoleDetailsDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(RoleDetailsDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public RoleDetailsDTO update(Long id, UpdateRoleDTO dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada com o ID: " + id));

        if (roleRepository.existsByName(dto.name()) && !role.getName().equalsIgnoreCase(dto.name())) {
            throw new EntityAlreadyExistsException("Já existe outra role com o nome " + dto.name());
        }

        role.setName(dto.name());
        Role updatedRole = roleRepository.save(role);
        return new RoleDetailsDTO(updatedRole);
    }

    @Transactional
    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role não encontrada com o ID: " + id);
        }

        if (userRepository.existsByRoles_Id(id)) {
            throw new RoleInUseException("Não é possível excluir um papel que está atribuído a um ou mais usuários.");
        }

        roleRepository.deleteById(id);
    }
}