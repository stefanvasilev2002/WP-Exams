package mk.ukim.finki.wp.june2022.g1.service.impl;

import mk.ukim.finki.wp.june2022.g1.repository.UserRepository;
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
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        mk.ukim.finki.wp.june2022.g1.model.User user = userRepository.findByUsername(username);
        return new User(user.getUsername(),
                user.getPassword(),
                Stream.of(
                                new SimpleGrantedAuthority("ROLE_USER"))
                        .collect(Collectors.toList()));
    }
}