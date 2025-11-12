package pharmacymarketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.models.Manufacturer;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Optional<Manufacturer> findByName(String name);
}
