package mk.ukim.finki.wp.kol2022.g3.service.impl;

import mk.ukim.finki.wp.kol2022.g3.model.ForumUser;
import mk.ukim.finki.wp.kol2022.g3.repository.ForumUserRepository;
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
    private final ForumUserRepository forumUserRepository;

    public LoginService(ForumUserRepository forumUserRepository) {
        this.forumUserRepository = forumUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ForumUser user = forumUserRepository.findByEmail(username);
        return new User(user.getEmail(),
                user.getPassword(),
                Stream.of(
                        new SimpleGrantedAuthority("ROLE_"+user.getType()))
                        .collect(Collectors.toList()));
    }
}
