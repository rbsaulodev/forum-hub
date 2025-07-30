package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.topic.CreateTopicDTO;
import br.com.rb.api.application.mapper.TopicMapper;
import br.com.rb.api.application.validations.CreateTopicValidator;
import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.Topic;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.CourseRepository;
import br.com.rb.api.domain.repository.TopicRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CreateTopicValidator createTopicValidator;

    public TopicService(TopicRepository topicRepository, UserRepository userRepository, CourseRepository courseRepository, CreateTopicValidator createTopicValidator) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.createTopicValidator = createTopicValidator;
    }

    @Transactional
    public Topic createTopic(CreateTopicDTO dto) {
        createTopicValidator.validate(dto);

        User author = userRepository.findById(dto.authorId())
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado com o ID: " + dto.authorId()));

        Course course = courseRepository.findById(dto.courseId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + dto.courseId()));

        Topic newTopic = TopicMapper.toEntity(dto, author, course);

        return topicRepository.save(newTopic);
    }
}