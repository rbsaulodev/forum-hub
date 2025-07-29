package br.com.rb.api.domain.repository;

import br.com.rb.api.domain.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
        SELECT COUNT(c) > 0 FROM Course c
        JOIN c.enrolledUsers u
        WHERE c.id = :courseId AND u.id = :userId
    """)
    boolean isUserEnrolled(Long userId, Long courseId);
}
