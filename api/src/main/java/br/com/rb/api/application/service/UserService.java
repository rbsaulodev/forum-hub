package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.user.AdminCreateUserDTO;
import br.com.rb.api.application.dto.user.CreateUserDTO;
import br.com.rb.api.application.dto.user.UserDetailsDTO;
import br.com.rb.api.application.dto.user.UpdateUserDTO;
import br.com.rb.api.application.mapper.UserMapper;
import br.com.rb.api.application.exception.EmailAlreadyExistsException;
import br.com.rb.api.application.exception.EntityAlreadyExistsException;
import br.com.rb.api.application.exception.UserDeletionException;
import br.com.rb.api.domain.model.Role;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.RoleRepository;
import br.com.rb.api.domain.repository.TopicRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TopicRepository topicRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TopicRepository topicRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.topicRepository = topicRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDetailsDTO registerStudent(CreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EntityAlreadyExistsException("O email informado já está em uso.");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        User newUser = UserMapper.toEntity(dto.name(), dto.email(), encodedPassword);

        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Erro de configuração: Papel ROLE_STUDENT não encontrado."));
        newUser.setRoles(Set.of(studentRole));

        User savedUser = userRepository.save(newUser);
        return userMapper.toDetailsDTO(savedUser);
    }

    @Transactional
    public UserDetailsDTO createUserByAdmin(AdminCreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EntityAlreadyExistsException("O email informado já está em uso.");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        User newUser = UserMapper.toEntity(dto.name(), dto.email(), encodedPassword);

        Set<Role> roles = roleRepository.findByNameIn(dto.roles());
        if (roles.size() != dto.roles().size()) {
            throw new RuntimeException("Um ou mais papéis informados não existem.");
        }
        newUser.setRoles(roles);

        User savedUser = userRepository.save(newUser);
        return userMapper.toDetailsDTO(savedUser);
    }

    public Page<UserDetailsDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDetailsDTO);
    }

    public UserDetailsDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
        return userMapper.toDetailsDTO(user);
    }

    @Transactional
    public UserDetailsDTO update(Long id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));

        if (dto.email() != null && !dto.email().equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmail(dto.email())) {
                throw new EmailAlreadyExistsException("O email informado já está em uso por outro usuário.");
            }
            user.setEmail(dto.email());
        }

        if (dto.name() != null) user.setName(dto.name());
        if (dto.password() != null) user.setPassword(passwordEncoder.encode(dto.password()));
        if (dto.roles() != null && !dto.roles().isEmpty()) {
            Set<Role> roles = roleRepository.findByNameIn(dto.roles());
            if (roles.size() != dto.roles().size()) {
                throw new RuntimeException("Um ou mais papéis informados não existem.");
            }
            user.setRoles(roles);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDetailsDTO(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + id);
        }
        if (topicRepository.existsByAuthorId(id)) {
            throw new UserDeletionException("Não é possível excluir um usuário que já é autor de tópicos.");
        }
        userRepository.deleteById(id);
    }
}