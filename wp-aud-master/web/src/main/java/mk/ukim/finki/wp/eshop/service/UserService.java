package mk.ukim.finki.wp.eshop.service;

import mk.ukim.finki.wp.eshop.model.Role;
import mk.ukim.finki.wp.eshop.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(String username, String password, String repeatPassword, String name, String surname, Role role);
}
