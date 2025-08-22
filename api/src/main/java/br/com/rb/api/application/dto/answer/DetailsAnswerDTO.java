package br.com.rb.api.application.dto.answer;

import br.com.rb.api.domain.model.Answer;
import java.time.LocalDateTime;

public record DetailsAnswerDTO(
        Long id,
        String text,
        LocalDateTime creationDate,
        Long topicId,
        String userName
) {
    public DetailsAnswerDTO(Answer answer) {
        this(
                answer.getId(),
                answer.getText(),
                answer.getCreationDate(),
                answer.getTopic().getId(),
                answer.getUser().getName()
        );
    }
}