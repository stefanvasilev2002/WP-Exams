package mk.ukim.finki.wp.eshop.service.impl;

import mk.ukim.finki.wp.eshop.model.User;
import mk.ukim.finki.wp.eshop.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.wp.eshop.model.exceptions.InvalidUserCredentialsException;
import mk.ukim.finki.wp.eshop.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.wp.eshop.model.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.wp.eshop.repository.impl.InMemoryUserRepository;
import mk.ukim.finki.wp.eshop.repository.jpa.UserRepository;
import mk.ukim.finki.wp.eshop.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username==null || username.isEmpty() || password==null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        return userRepository.findByUsernameAndPassword(username,
                password).orElseThrow(InvalidUserCredentialsException::new);
    }

}
