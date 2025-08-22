package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.user.CreateTeacherDTO;
import br.com.rb.api.application.dto.user.TeacherDetailsDTO;
import br.com.rb.api.application.service.TeacherService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/teachers")
@SecurityRequirement(name = "bearer-key")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherDetailsDTO> createTeacher(@RequestBody @Valid CreateTeacherDTO dto, UriComponentsBuilder uriBuilder) {
        TeacherDetailsDTO newTeacher = teacherService.create(dto);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(newTeacher.id()).toUri();
        return ResponseEntity.created(uri).body(newTeacher);
    }

    @GetMapping
    public ResponseEntity<Page<TeacherDetailsDTO>> listTeachers(@PageableDefault(size = 10, sort = {"name"}) Pageable pageable) {
        Page<TeacherDetailsDTO> page = teacherService.findAll(pageable);
        return ResponseEntity.ok(page);
    }
}