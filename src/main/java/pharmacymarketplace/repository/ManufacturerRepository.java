package pharmacymarketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.model.Manufacturer;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Optional<Manufacturer> findByName(String name);
}
