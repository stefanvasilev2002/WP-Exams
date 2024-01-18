package mk.ukim.finki.wp.eshop.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


public enum Role implements GrantedAuthority {

    ROLE_USER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
