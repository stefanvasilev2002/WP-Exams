package mk.ukim.finki.wp.kol2022.g2.repository;

import mk.ukim.finki.wp.kol2022.g2.model.Course;
import mk.ukim.finki.wp.kol2022.g2.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByCoursesContainingAndEnrollmentDateBefore(Course byId, LocalDate localDate);

    List<Student> findAllByCoursesContaining(Course byId);

    List<Student> findAllByEnrollmentDateBefore(LocalDate localDate);

    Student findByEmail(String username);
}
