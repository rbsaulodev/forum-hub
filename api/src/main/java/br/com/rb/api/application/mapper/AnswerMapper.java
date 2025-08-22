package br.com.rb.api.application.mapper;


import br.com.rb.api.application.dto.answer.DetailsAnswerDTO;
import br.com.rb.api.domain.model.Answer;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {
    public DetailsAnswerDTO toDto(Answer answer) {
        return new DetailsAnswerDTO(
                answer.getId(),
                answer.getText(),
                answer.getCreationDate(),
                answer.getTopic().getId(),
                answer.getUser().getName()
        );
    }
}