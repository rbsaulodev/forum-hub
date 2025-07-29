package br.com.rb.api.domain.repository;

import br.com.rb.api.domain.model.Course;
import br.com.rb.api.domain.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
