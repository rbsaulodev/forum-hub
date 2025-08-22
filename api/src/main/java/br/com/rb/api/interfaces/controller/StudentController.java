package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.course.CourseDetailsDTO;
import br.com.rb.api.application.service.StudentService;
import br.com.rb.api.domain.model.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@SecurityRequirement(name = "bearer-key")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/courses/{courseId}/enroll")
    public ResponseEntity<CourseDetailsDTO> enroll(@PathVariable Long courseId,
                                                   @AuthenticationPrincipal User authenticatedUser) {
        CourseDetailsDTO updatedCourse = studentService.enroll(authenticatedUser.getId(), courseId);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/courses/{courseId}/unenroll")
    public ResponseEntity<CourseDetailsDTO> unenroll(@PathVariable Long courseId,
                                                     @AuthenticationPrincipal User authenticatedUser) {
        CourseDetailsDTO updatedCourse = studentService.unenroll(authenticatedUser.getId(), courseId);
        return ResponseEntity.ok(updatedCourse);
    }
}