package pharmacymarketplace.product.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.product.domain.jpa.Manufacturer;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Optional<Manufacturer> findByName(String name);
}

