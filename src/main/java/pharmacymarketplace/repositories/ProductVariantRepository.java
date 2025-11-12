package pharmacymarketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.models.ProductVariant;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findBySku(String sku);

}
