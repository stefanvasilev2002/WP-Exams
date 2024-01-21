package mk.ukim.finki.wp.jan2022.g2.repository;

import mk.ukim.finki.wp.jan2022.g2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
