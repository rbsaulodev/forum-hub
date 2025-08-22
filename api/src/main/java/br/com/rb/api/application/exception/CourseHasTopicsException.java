package br.com.rb.api.application.exception;

public class CourseHasTopicsException extends RuntimeException {
    public CourseHasTopicsException(String message) {
        super(message);
    }
}
