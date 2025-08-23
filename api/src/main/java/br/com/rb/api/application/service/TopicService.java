package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.topic.CreateTopicDTO;
import br.com.rb.api.application.dto.topic.UpdateTopicDTO;
import br.com.rb.api.application.mapper.TopicMapper;
import br.com.rb.api.application.validations.CreateTopicValidator;
import br.com.rb.api.application.exception.TopicModificationForbiddenException;
import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.Role;
import br.com.rb.api.domain.model.Topic;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.CourseRepository;
import br.com.rb.api.domain.repository.TopicRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
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
    public Topic create(CreateTopicDTO dto) {
        createTopicValidator.validate(dto);

        User author = userRepository.findById(dto.authorId())
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado com o ID: " + dto.authorId()));

        Course course = courseRepository.findById(dto.courseId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + dto.courseId()));

        Topic newTopic = TopicMapper.toEntity(dto, author, course);

        return topicRepository.save(newTopic);
    }

    public Page<Topic> findAll(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    public Topic findById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado com o ID: " + id));
    }


    @Transactional
    public Topic update(Long topicId, UpdateTopicDTO dto, Long requestingUserId) {
        Topic topic = findById(topicId);
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário solicitante não encontrado."));

        validateModificationPermission(topic, requestingUser);

        topic.setTitle(dto.title());
        topic.setText(dto.text());
        return topicRepository.save(topic);
    }

    @Transactional
    public void delete(Long topicId, Long requestingUserId) {
        Topic topic = findById(topicId);
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário solicitante não encontrado."));

        validateModificationPermission(topic, requestingUser);
        topicRepository.delete(topic);
    }

    private void validateModificationPermission(Topic topic, User user) {
        boolean isAuthor = topic.getAuthor().getId().equals(user.getId());
        boolean isAdminOrMod = user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(name -> name.equals("ROLE_ADMIN") || name.equals("ROLE_MODERATOR"));

        if (!isAuthor && !isAdminOrMod) {
            throw new TopicModificationForbiddenException("Usuário não tem permissão para modificar este tópico.");
        }
    }

    public boolean isAuthor(Long topicId, Long userId) {
        return topicRepository.existsByIdAndAuthorId(topicId, userId);
    }
}