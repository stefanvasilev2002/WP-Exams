package mk.ukim.finki.wp.june2022.g1.repository;

import mk.ukim.finki.wp.june2022.g1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
