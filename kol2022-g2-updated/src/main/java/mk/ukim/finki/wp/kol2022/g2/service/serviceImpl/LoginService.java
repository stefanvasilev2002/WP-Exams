package mk.ukim.finki.wp.kol2022.g2.service.serviceImpl;

import mk.ukim.finki.wp.kol2022.g2.model.Student;
import mk.ukim.finki.wp.kol2022.g2.repository.StudentRepository;
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

    private final StudentRepository studentRepository;

    public LoginService(StudentRepository studentRepository) {

        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(s);
        return new User(
                student.getName(),
                student.getPassword(),
                Stream.of(new SimpleGrantedAuthority("ROLE_" + student.getType())).collect(Collectors.toList())
        );
    }
}
