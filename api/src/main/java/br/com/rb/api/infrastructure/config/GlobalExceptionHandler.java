package br.com.rb.api.infrastructure.config;

import br.com.rb.api.application.exception.GeneralErrorDTO;
import br.com.rb.api.application.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handler para erros de validação dos DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldErrorDTO>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldErrorDTO> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDTO(error.getField(), error.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    // Handler para violação de regra de negócio (usuário não matriculado)
    @ExceptionHandler(UserNotEnrolledException.class)
    public ResponseEntity<GeneralErrorDTO> handleUserNotEnrolled(UserNotEnrolledException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GeneralErrorDTO(ex.getMessage()));
    }

    // Handler para violação de permissão de modificação
    @ExceptionHandler(TopicModificationForbiddenException.class)
    public ResponseEntity<GeneralErrorDTO> handleTopicModificationForbidden(TopicModificationForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GeneralErrorDTO(ex.getMessage()));
    }

    // Handlers para conflitos de estado (409 Conflict)
    @ExceptionHandler({
            DuplicateTopicException.class,
            CourseHasTopicsException.class,
            RoleInUseException.class,
            UserDeletionException.class,
            EntityAlreadyExistsException.class
    })
    public ResponseEntity<GeneralErrorDTO> handleConflictExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new GeneralErrorDTO(ex.getMessage()));
    }
}