package br.com.rb.api.domain.repository;

import br.com.rb.api.domain.model.Answer;
import br.com.rb.api.domain.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
