package br.com.rb.api.application.validations;

public class TopicModificationForbiddenException extends RuntimeException {
    public TopicModificationForbiddenException(String message) {
        super(message);
    }
}
