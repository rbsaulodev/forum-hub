package br.com.rb.api.controller;

import br.com.rb.api.application.dto.course.CourseDetailsDTO;
import br.com.rb.api.application.service.UserService;
import br.com.rb.api.domain.model.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@SecurityRequirement(name = "bearer-key")
public class StudentController {

    private final UserService userService;

    public StudentController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/courses/{courseId}/enroll")
    public ResponseEntity<CourseDetailsDTO> enroll(@PathVariable Long courseId,
                                                   @AuthenticationPrincipal User authenticatedUser) {
        CourseDetailsDTO updatedCourse = userService.enroll(authenticatedUser.getId(), courseId);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/courses/{courseId}/unenroll")
    public ResponseEntity<CourseDetailsDTO> unenroll(@PathVariable Long courseId,
                                                     @AuthenticationPrincipal User authenticatedUser) {
        CourseDetailsDTO updatedCourse = userService.unenroll(authenticatedUser.getId(), courseId);
        return ResponseEntity.ok(updatedCourse);
    }
}