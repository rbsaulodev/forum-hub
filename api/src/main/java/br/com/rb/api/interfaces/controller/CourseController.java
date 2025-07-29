package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.service.CourseService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


}
