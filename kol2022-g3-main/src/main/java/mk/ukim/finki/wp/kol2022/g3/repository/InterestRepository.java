package mk.ukim.finki.wp.kol2022.g3.repository;

import mk.ukim.finki.wp.kol2022.g3.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
}
