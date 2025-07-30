package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.course.CreateCourseDTO;
import br.com.rb.api.application.dto.course.CourseDetailsDTO;
import br.com.rb.api.application.service.CourseService;
import br.com.rb.api.domain.model.Course;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDetailsDTO> create(@RequestBody @Valid CreateCourseDTO dto, UriComponentsBuilder uriBuilder) {
        Course newCourse = courseService.create(dto);
        var uri = uriBuilder.path("/courses/{id}").buildAndExpand(newCourse.getId()).toUri();
        CourseDetailsDTO detailsDto = new CourseDetailsDTO(newCourse);
        return ResponseEntity.created(uri).body(detailsDto);
    }
}
