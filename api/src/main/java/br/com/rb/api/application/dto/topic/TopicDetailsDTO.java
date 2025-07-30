package br.com.rb.api.application.dto.topic;

import br.com.rb.api.domain.model.Topic;
import br.com.rb.api.domain.model.TopicStatus;
import java.time.LocalDateTime;

public record TopicDetailsDTO(
        Long id,
        String title,
        String text,
        LocalDateTime creationDate,
        TopicStatus status,
        String authorName,
        String courseName
) {
    public TopicDetailsDTO(Topic topic) {
        this(
                topic.getId(),
                topic.getTitle(),
                topic.getText(),
                topic.getCreationDate(),
                topic.getStatus(),
                topic.getAuthor().getName(),
                topic.getCourse().getName()
        );
    }
}