package br.com.rb.api.application.exception;

public class TopicModificationForbiddenException extends RuntimeException {
    public TopicModificationForbiddenException(String message) {
        super(message);
    }
}
