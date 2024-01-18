package mk.ukim.finki.wp.kol2022.g2.repository;

import mk.ukim.finki.wp.kol2022.g2.model.Course;
import mk.ukim.finki.wp.kol2022.g2.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByCoursesContains(Course course);

    List<Student> findAllByEnrollmentDateBefore(LocalDate date);

    List<Student> findAllByCoursesContainsAndEnrollmentDateBefore(Course course, LocalDate date);

    Optional<Student> findByEmail(String email);
}
