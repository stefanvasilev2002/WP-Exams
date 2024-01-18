package mk.ukim.finki.wp.kol2022.g1.repository;

import mk.ukim.finki.wp.kol2022.g1.model.Employee;
import mk.ukim.finki.wp.kol2022.g1.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Optional<Employee> findByEmail(String name);
   List<Employee> findAllBySkillsContainingAndEmploymentDateBefore(Skill skill,LocalDate localDate);
   List<Employee> findAllBySkillsContaining(Skill skill);
   List<Employee> findAllByEmploymentDateBefore(LocalDate localDate);
}
