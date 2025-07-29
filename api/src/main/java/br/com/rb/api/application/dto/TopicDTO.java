package br.com.rb.api.application.dto;

import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.TopicStatus;
import br.com.rb.api.domain.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public record TopicDTO (
        Long id,
        String title,
        String text,
        LocalDateTime creationDate,
        TopicStatus status,
        User author,
        Course course
){
}
