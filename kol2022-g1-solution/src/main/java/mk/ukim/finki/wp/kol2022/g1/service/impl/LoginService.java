package mk.ukim.finki.wp.kol2022.g1.service.impl;

import mk.ukim.finki.wp.kol2022.g1.model.Employee;
import mk.ukim.finki.wp.kol2022.g1.repository.EmployeeRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LoginService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public LoginService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Employee emp = employeeRepository.findByEmail(s);
        return new User(
                emp.getEmail(),
                emp.getPassword(),
                Stream.of(new SimpleGrantedAuthority("ROLE_" + emp.getType())).collect(Collectors.toList())
        );
    }
}
