package pharmacymarketplace.product.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.product.domain.jpa.ProductCategory;
import pharmacymarketplace.product.domain.jpa.ProductCategoryId;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
    boolean existsById(ProductCategoryId id);
}

