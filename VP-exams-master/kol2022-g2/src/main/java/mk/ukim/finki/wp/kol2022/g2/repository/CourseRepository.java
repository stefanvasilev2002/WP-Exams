package mk.ukim.finki.wp.kol2022.g2.repository;

import mk.ukim.finki.wp.kol2022.g2.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
