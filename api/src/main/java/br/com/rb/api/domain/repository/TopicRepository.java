package br.com.rb.api.domain.repository;

import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    boolean existsByTitleAndText(String title, String text);
}
