package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.course.CourseDetailsDTO;
import br.com.rb.api.application.dto.user.*;
import br.com.rb.api.application.exception.EmailAlreadyExistsException;
import br.com.rb.api.application.exception.EntityAlreadyExistsException;
import br.com.rb.api.application.exception.UserDeletionException;
import br.com.rb.api.application.mapper.CourseMapper;
import br.com.rb.api.application.mapper.UserMapper;
import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.Role;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.CourseRepository;
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
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final CourseMapper courseMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, CourseRepository courseRepository, TopicRepository topicRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, CourseMapper courseMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.courseRepository = courseRepository;
        this.topicRepository = topicRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.courseMapper = courseMapper;
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


    @Transactional
    public TeacherDetailsDTO createTeacher(CreateTeacherDTO dto) {
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

    public Page<TeacherDetailsDTO> findAllTeachers(Pageable pageable) {
        return userRepository.findByRoles_Name("ROLE_TEACHER", pageable)
                .map(userMapper::toTeacherDetailsDTO);
    }


    @Transactional
    public CourseDetailsDTO enroll(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + userId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + courseId));

        if (course.getTeachers().contains(user)) {
            throw new IllegalArgumentException("Você não pode se matricular em um curso que você leciona.");
        }
        if (course.getEnrolledUsers().contains(user)) {
            throw new EntityAlreadyExistsException("Usuário já matriculado neste curso.");
        }

        course.getEnrolledUsers().add(user);
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toDetailsDTO(updatedCourse);
    }

    @Transactional
    public CourseDetailsDTO unenroll(Long userId, Long courseId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + userId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + courseId));

        course.getEnrolledUsers().remove(user);
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toDetailsDTO(updatedCourse);
    }
}