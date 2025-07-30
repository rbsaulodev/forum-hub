package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.user.AdminCreateUserDTO;
import br.com.rb.api.application.dto.user.CreateUserDTO;
import br.com.rb.api.application.mapper.UserMapper;
import br.com.rb.api.application.validations.EmailAlreadyExistsException;
import br.com.rb.api.domain.model.Role;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.RoleRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerStudent(CreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("O email informado já está em uso.");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        User newUser = UserMapper.toEntity(dto.name(), dto.email(), encodedPassword);

        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Erro de configuração: Papel ROLE_STUDENT não encontrado."));
        newUser.setRoles(Set.of(studentRole));

        return userRepository.save(newUser);
    }

    @Transactional
    public User createUserByAdmin(AdminCreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("O email informado já está em uso.");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        User newUser = UserMapper.toEntity(dto.name(), dto.email(), encodedPassword);

        Set<Role> roles = roleRepository.findByNameIn(dto.roles());
        if (roles.size() != dto.roles().size()) {
            throw new RuntimeException("Um ou mais papéis informados não existem.");
        }
        newUser.setRoles(roles);

        return userRepository.save(newUser);
    }
}
