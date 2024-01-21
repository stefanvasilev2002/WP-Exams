package mk.ukim.finki.wp.sep2022.repository;

import mk.ukim.finki.wp.sep2022.model.MatchLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchLocationRepository extends JpaRepository<MatchLocation, Long> {
}
