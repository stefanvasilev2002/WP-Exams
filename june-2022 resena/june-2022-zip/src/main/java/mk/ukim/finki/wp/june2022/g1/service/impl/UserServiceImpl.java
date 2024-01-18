package mk.ukim.finki.wp.june2022.g1.service.impl;

import mk.ukim.finki.wp.june2022.g1.model.User;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidUserIdException;
import mk.ukim.finki.wp.june2022.g1.repository.UserRepository;
import mk.ukim.finki.wp.june2022.g1.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(InvalidUserIdException::new);
    }

    @Override
    public List<User> listAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User create(String username, String password, String role) {
        User user=new User(username,passwordEncoder.encode(password),role);
        return this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=this.userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
        UserDetails userDetails=new org.springframework.security.core.userdetails.User
                (user.getUsername()
                        ,user.getPassword()
                        , Stream.of(new SimpleGrantedAuthority(user.getRole().toString())).collect(Collectors.toList()));
        return userDetails;
    }
}
