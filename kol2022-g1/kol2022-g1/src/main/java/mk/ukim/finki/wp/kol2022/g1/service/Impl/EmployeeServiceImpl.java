package mk.ukim.finki.wp.kol2022.g1.service.Impl;

import mk.ukim.finki.wp.kol2022.g1.model.Employee;
import mk.ukim.finki.wp.kol2022.g1.model.EmployeeType;
import mk.ukim.finki.wp.kol2022.g1.model.Skill;
import mk.ukim.finki.wp.kol2022.g1.model.exceptions.InvalidEmployeeIdException;
import mk.ukim.finki.wp.kol2022.g1.repository.EmployeeRepository;
import mk.ukim.finki.wp.kol2022.g1.repository.SkillRepository;
import mk.ukim.finki.wp.kol2022.g1.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;

    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, SkillRepository skillRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.skillRepository = skillRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Employee> listAll() {
        return this.employeeRepository.findAll();
    }

    @Override
    public Employee findById(Long id) {

       return this.employeeRepository.findById(id)
                .orElseThrow(InvalidEmployeeIdException::new);
    }

    @Override
    public Employee create(String name, String email, String password, EmployeeType type, List<Long> skillId, LocalDate employmentDate) {
        List<Skill> skillList=this.skillRepository.findAllById(skillId);
        Employee employee=new Employee(name,email,passwordEncoder.encode(password),type,skillList,employmentDate);
        return this.employeeRepository.save(employee);
    }

    @Override
    public Employee update(Long id, String name, String email, String password, EmployeeType type, List<Long> skillId, LocalDate employmentDate) {
        Employee employee=findById(id);
        List<Skill> skillList=this.skillRepository.findAllById(skillId);
        employee.setName(name);
        employee.setEmail(email);
        employee.setPassword(password);
        employee.setType(type);
        employee.setSkills(skillList);
        employee.setEmploymentDate(employmentDate);
        return this.employeeRepository.save(employee);
    }

    @Override
    public Employee delete(Long id) {

        Employee employee=findById(id);
        this.employeeRepository.delete(employee);
        return employee;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee user=this.employeeRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
        UserDetails userDetails=new org.springframework.security.core.userdetails.User
                (user.getEmail()
                        ,user.getPassword()
                        , Stream.of(new SimpleGrantedAuthority("ROLE_"+user.getType())).collect(Collectors.toList()));
        return userDetails;
    }

    @Override
    public List<Employee> filter(Long skillId, Integer yearsOfService) {
        Skill skill;
        LocalDate localDate;
        if(skillId==null)
        {
            skill=null;
        }
        else {
            skill=this.skillRepository.findById(skillId)
                    .orElseGet(null);
        }
        if(yearsOfService==null)
        {
            localDate=null;
        }
        else
        {
            localDate=LocalDate.now().minusYears((long) yearsOfService);
        }
        if(skill!=null&&localDate!=null)
        {
            return this.employeeRepository.findAllBySkillsContainingAndEmploymentDateBefore(skill,localDate);
        }
        else if(skill!=null&&localDate==null)
        {
            return this.employeeRepository.findAllBySkillsContaining(skill);
        }
        else if(skill==null&&localDate!=null)
        {
            return this.employeeRepository.findAllByEmploymentDateBefore(localDate);
        }
        else
        {
            return this.employeeRepository.findAll();
        }

    }
}
