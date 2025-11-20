package pharmacymarketplace.product.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.product.domain.jpa.Brand;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);
}

