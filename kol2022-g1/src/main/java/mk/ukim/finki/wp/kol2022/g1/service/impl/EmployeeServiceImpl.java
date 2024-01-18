package mk.ukim.finki.wp.kol2022.g1.service.impl;

import mk.ukim.finki.wp.kol2022.g1.model.Employee;
import mk.ukim.finki.wp.kol2022.g1.model.EmployeeType;
import mk.ukim.finki.wp.kol2022.g1.model.Skill;
import mk.ukim.finki.wp.kol2022.g1.model.exceptions.InvalidEmployeeIdException;
import mk.ukim.finki.wp.kol2022.g1.model.exceptions.InvalidSkillIdException;
import mk.ukim.finki.wp.kol2022.g1.repository.EmployeeRepository;
import mk.ukim.finki.wp.kol2022.g1.repository.SkillRepository;
import mk.ukim.finki.wp.kol2022.g1.service.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final SkillServiceImpl skillService;
    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, SkillServiceImpl skillService, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.skillService = skillService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Employee> listAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow(InvalidEmployeeIdException::new);
    }

    @Override
    public Employee create(String name, String email, String password, EmployeeType type, List<Long> skillId, LocalDate employmentDate) {
        return employeeRepository.save(new Employee(
                name,
                email,
                passwordEncoder.encode(password),
                type,
                skillId.stream().map(this.skillService::findById).collect(Collectors.toList()),
                employmentDate
                ));
    }

    @Override
    public Employee update(Long id, String name, String email, String password, EmployeeType type, List<Long> skillId, LocalDate employmentDate) {
        Employee emp = this.findById(id);
        emp.setName(name);
        emp.setEmail(email);
        emp.setPassword(this.passwordEncoder.encode(password));
        emp.setType(type);
        emp.setSkills(skillId.stream().map(skillService::findById).collect(Collectors.toList()));
        emp.setEmploymentDate(employmentDate);
        return this.employeeRepository.save(emp);
    }

    @Override
    public Employee delete(Long id) {
        Employee emp = findById(id);
        this.employeeRepository.delete(emp);
        return emp;
    }

    @Override
    public List<Employee> filter(Long skillId, Integer yearsOfService) {
        if (skillId == null && yearsOfService == null){
            return employeeRepository.findAll();
        }
        else if(yearsOfService == null){
            Skill skill = skillService.findById(skillId);
            return employeeRepository.findBySkillsContaining(skill);
        }
        else if (skillId == null){
            LocalDate employmentBefore = LocalDate.now().minusYears(yearsOfService);
            return employeeRepository.findByEmploymentDateBefore(employmentBefore);
        }
        else {
            LocalDate employmentBefore = LocalDate.now().minusYears(yearsOfService);
            Skill skill = skillService.findById(skillId);
            return employeeRepository.findByEmploymentDateBeforeAndSkillsContaining(employmentBefore, skill);
        }
    }
}
