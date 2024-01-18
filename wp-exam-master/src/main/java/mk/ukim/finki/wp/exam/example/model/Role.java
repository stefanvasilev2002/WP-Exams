package mk.ukim.finki.wp.exam.example.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
