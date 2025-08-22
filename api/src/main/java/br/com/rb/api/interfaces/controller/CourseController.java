package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.course.CreateCourseDTO;
import br.com.rb.api.application.dto.course.CourseDetailsDTO;
import br.com.rb.api.application.dto.course.UpdateCourseDTO;
import br.com.rb.api.application.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        CourseDetailsDTO newCourseDto = courseService.create(dto);
        var uri = uriBuilder.path("/courses/{id}").buildAndExpand(newCourseDto.id()).toUri();
        return ResponseEntity.created(uri).body(newCourseDto);
    }

    @GetMapping
    public ResponseEntity<Page<CourseDetailsDTO>> list(@PageableDefault(size = 10, sort = {"name"}) Pageable pageable) {
        Page<CourseDetailsDTO> page = courseService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailsDTO> detail(@PathVariable Long id) {
        CourseDetailsDTO detailsDto = courseService.findById(id);
        return ResponseEntity.ok(detailsDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDetailsDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateCourseDTO dto) {
        CourseDetailsDTO updatedCourseDto = courseService.update(id, dto);
        return ResponseEntity.ok(updatedCourseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}