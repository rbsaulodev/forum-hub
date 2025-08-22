package br.com.rb.api.application.mapper;

import br.com.rb.api.application.dto.user.TeacherDetailsDTO;
import br.com.rb.api.application.dto.user.UserDetailsDTO;
import br.com.rb.api.domain.model.Role;
import br.com.rb.api.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public static User toEntity(String name, String email, String encodedPassword) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        return user;
    }

    public UserDetailsDTO toDetailsDTO(User user) {
        return new UserDetailsDTO(user);
    }

    public TeacherDetailsDTO toTeacherDetailsDTO(User user) {
        if (user == null) {
            return null;
        }

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new TeacherDetailsDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getSpecialty(),
                user.getStack(),
                roleNames
        );
    }
}