package mk.ukim.finki.wp.kol2022.g1.model.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;


public class InvalidEmployeeIdException extends RuntimeException {
    public InvalidEmployeeIdException() {
        super();
    }
}
