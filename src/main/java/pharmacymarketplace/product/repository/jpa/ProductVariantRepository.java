package pharmacymarketplace.product.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.product.domain.jpa.Product;
import pharmacymarketplace.product.domain.jpa.ProductVariant;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findBySku(String sku);
    Optional<ProductVariant> findByGtin(String gtin);
    List<ProductVariant> findByProduct(Product product);
    List<ProductVariant> findByProductId(Long productId);
}

