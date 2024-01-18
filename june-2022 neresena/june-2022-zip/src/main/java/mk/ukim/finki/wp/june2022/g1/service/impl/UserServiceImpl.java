package mk.ukim.finki.wp.june2022.g1.service.impl;

import mk.ukim.finki.wp.june2022.g1.model.User;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidUserIdException;
import mk.ukim.finki.wp.june2022.g1.repository.UserRepository;
import mk.ukim.finki.wp.june2022.g1.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(InvalidUserIdException::new);
    }

    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(String username, String password, String role) {
        return userRepository.save(new User(username, password, role));
    }
}
