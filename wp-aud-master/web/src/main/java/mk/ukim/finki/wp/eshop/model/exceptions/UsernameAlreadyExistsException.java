package mk.ukim.finki.wp.eshop.model.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String username) {
        super(String.format("User with username: %s already exists", username));
    }
}
