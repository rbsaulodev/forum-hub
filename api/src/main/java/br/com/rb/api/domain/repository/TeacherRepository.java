package br.com.rb.api.domain.repository;

import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
