package br.com.rb.api.application.dto.user;

import br.com.rb.api.domain.model.SpecialtyTeacher;
import br.com.rb.api.domain.model.StackTeacher;

import java.util.Set;

public record TeacherDetailsDTO(
        Long id,
        String name,
        String email,
        SpecialtyTeacher specialty,
        StackTeacher stack,
        Set<String> roles
) {}