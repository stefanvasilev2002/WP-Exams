package mk.ukim.finki.wp.exam.example.service.impl;

import mk.ukim.finki.wp.exam.example.model.Role;
import mk.ukim.finki.wp.exam.example.model.User;
import mk.ukim.finki.wp.exam.example.repository.UserRepository;
import mk.ukim.finki.wp.exam.example.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(String username, String password, Role role) {
        return userRepository.save(new User(username, passwordEncoder.encode(password), role));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(s));

        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getPassword())
                .authorities(u.getRole().name())
                .build();
    }
}
