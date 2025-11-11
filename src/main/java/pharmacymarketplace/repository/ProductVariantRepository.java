package pharmacymarketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.model.ProductVariant;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findBySku(String sku);

}
