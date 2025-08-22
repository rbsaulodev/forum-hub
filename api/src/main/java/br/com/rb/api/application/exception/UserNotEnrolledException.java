package br.com.rb.api.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotEnrolledException extends RuntimeException {

    public UserNotEnrolledException(String message) {
        super(message);
    }
}