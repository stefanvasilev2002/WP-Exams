package mk.ukim.finki.wp.kol2022.g1.model;

import org.springframework.security.core.GrantedAuthority;

public enum EmployeeType implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_REGULAR,
    ROLE_CONSULTANT;

    @Override
    public String getAuthority() {
        return name();
    }
}
