package mk.ukim.finki.wp.kol2022.g1.service.impl;

import mk.ukim.finki.wp.kol2022.g1.model.Employee;
import mk.ukim.finki.wp.kol2022.g1.model.EmployeeType;
import mk.ukim.finki.wp.kol2022.g1.model.Skill;
import mk.ukim.finki.wp.kol2022.g1.model.exceptions.InvalidEmployeeIdException;
import mk.ukim.finki.wp.kol2022.g1.repository.EmployeeRepository;
import mk.ukim.finki.wp.kol2022.g1.service.EmployeeService;
import mk.ukim.finki.wp.kol2022.g1.service.SkillService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {

    private final EmployeeRepository employeeRepository;

    private final SkillService skillService;

    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, SkillService skillService, PasswordEncoder passwordEncoder) {
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
        List<Skill> skills = skillId.stream().map(skillService::findById).collect(Collectors.toList());
        Employee employee = new Employee(name, email, passwordEncoder.encode(password), type, skills, employmentDate);

        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Long id, String name, String email, String password, EmployeeType type, List<Long> skillId, LocalDate employmentDate) {
        Employee employee = findById(id);

        employee.setName(name);
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setType(type);
        employee.setSkills(skillId.stream().map(skillService::findById).collect(Collectors.toList()));
        employee.setEmploymentDate(employmentDate);

        return employeeRepository.save(employee);
    }

    @Override
    public Employee delete(Long id) {
        Employee employee = findById(id);
        employeeRepository.delete(employee);
        return employee;
    }

    @Override
    public List<Employee> filter(Long skillId, Integer yearsOfService) {
        if (skillId == null && yearsOfService == null) {
            return listAll();
        } else if (skillId == null) {
            return employeeRepository.findByEmploymentDateBefore(LocalDate.now().minusYears(yearsOfService));
        } else if (yearsOfService == null) {
            return employeeRepository.findAllBySkillsContains(skillService.findById(skillId));
        } else {
            return employeeRepository.findAllBySkillsContainsAndEmploymentDateBefore(skillService.findById(skillId), LocalDate.now().minusYears(yearsOfService));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee e = employeeRepository.findByEmail(username);

        if (e == null) {
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
                .username(e.getName())
                .password(e.getPassword())
                .authorities("ROLE_" + e.getType().name())
                .build();
    }
}
