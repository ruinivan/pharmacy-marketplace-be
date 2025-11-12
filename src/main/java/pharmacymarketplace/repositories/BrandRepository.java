package pharmacymarketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.models.Brand;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);
}
