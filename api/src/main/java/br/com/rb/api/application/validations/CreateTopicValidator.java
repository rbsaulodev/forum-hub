package br.com.rb.api.application.validations;

import br.com.rb.api.application.dto.CreateTopicDTO;
import br.com.rb.api.domain.repository.CourseRepository;
import br.com.rb.api.domain.repository.TopicRepository;

public class CreateTopicValidator {

    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;

    public CreateTopicValidator(TopicRepository topicRepository, CourseRepository courseRepository) {
        this.topicRepository = topicRepository;
        this.courseRepository = courseRepository;
    }

    public void validate(CreateTopicDTO dto) {
        boolean topicExists = topicRepository.existsByTitleAndText(dto.title(), dto.text());
        if (topicExists) {
            throw new DuplicateTopicException("Já existe um tópico com este título e mensagem.");
        }

        boolean userIsEnrolled = courseRepository.isUserEnrolled(dto.authorId(), dto.courseId());
        if (!userIsEnrolled) {
            throw new UserNotEnrolledException("Usuário não está matriculado no curso para a criação do tópico!");
        }
    }
}
