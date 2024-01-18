package mk.ukim.finki.wp.eshop.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String username) {
        super(String.format("User with username: %s was not found", username));
    }
}
