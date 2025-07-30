package br.com.rb.api.infrastructure.config;

import br.com.rb.api.application.dto.exception.GeneralErrorDTO;
import br.com.rb.api.application.validations.CourseHasTopicsException;
import br.com.rb.api.application.validations.DuplicateTopicException;
import br.com.rb.api.application.validations.UserNotEnrolledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateTopicException.class)
    public ResponseEntity<GeneralErrorDTO> handleDuplicateTopic(DuplicateTopicException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new GeneralErrorDTO(ex.getMessage()));
    }

    @ExceptionHandler(UserNotEnrolledException.class)
    public ResponseEntity<GeneralErrorDTO> handleUserNotEnrolled(UserNotEnrolledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GeneralErrorDTO(ex.getMessage()));
    }

    @ExceptionHandler(CourseHasTopicsException.class)
    public ResponseEntity<GeneralErrorDTO> handleCourseHasTopics(CourseHasTopicsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new GeneralErrorDTO(ex.getMessage()));
    }

}
