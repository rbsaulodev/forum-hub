package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.user.CreateTeacherDTO;
import br.com.rb.api.application.dto.user.TeacherDetailsDTO;
import br.com.rb.api.application.exception.EntityAlreadyExistsException;
import br.com.rb.api.application.mapper.UserMapper;
import br.com.rb.api.domain.model.Role;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.RoleRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TeacherService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public TeacherService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public TeacherDetailsDTO create(CreateTeacherDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EntityAlreadyExistsException("O email informado já está em uso.");
        }

        Role teacherRole = roleRepository.findByName("ROLE_TEACHER")
                .orElseThrow(() -> new RuntimeException("Configuração de sistema: Papel ROLE_TEACHER não encontrado."));

        User newTeacher = new User();
        newTeacher.setName(dto.name());
        newTeacher.setEmail(dto.email());
        newTeacher.setPassword(passwordEncoder.encode(dto.password()));
        newTeacher.setSpecialty(dto.specialty());
        newTeacher.setStack(dto.stack());
        newTeacher.setRoles(Set.of(teacherRole));

        User savedTeacher = userRepository.save(newTeacher);
        return userMapper.toTeacherDetailsDTO(savedTeacher);
    }

    public Page<TeacherDetailsDTO> findAll(Pageable pageable) {
        return userRepository.findByRoles_Name("ROLE_TEACHER", pageable)
                .map(userMapper::toTeacherDetailsDTO);
    }
}