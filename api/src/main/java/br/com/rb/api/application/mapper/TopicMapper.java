package br.com.rb.api.application.mapper;

import br.com.rb.api.application.dto.topic.CreateTopicDTO;
import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.Topic;
import br.com.rb.api.domain.model.TopicStatus;
import br.com.rb.api.domain.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TopicMapper {
    public static Topic toEntity(CreateTopicDTO dto, User author, Course course) {
        Topic topic = new Topic();
        topic.setTitle(dto.title());
        topic.setText(dto.text());
        topic.setAuthor(author);
        topic.setCourse(course);
        topic.setStatus(TopicStatus.NOT_ANSWERED);
        topic.setCreationDate(LocalDateTime.now());
        return topic;
    }
}
