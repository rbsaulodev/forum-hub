package br.com.rb.api.domain.repository;

import br.com.rb.api.domain.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Course, Long> {
}
