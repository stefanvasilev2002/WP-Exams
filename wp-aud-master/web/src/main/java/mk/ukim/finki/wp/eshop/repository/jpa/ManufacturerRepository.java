package mk.ukim.finki.wp.eshop.repository.jpa;

import mk.ukim.finki.wp.eshop.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
}
