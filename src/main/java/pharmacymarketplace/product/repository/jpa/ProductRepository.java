package pharmacymarketplace.product.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.product.domain.jpa.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByPublicId(UUID publicId);
    Optional<Product> findByAnvisaCode(String anvisaCode);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByActivePrincipleContainingIgnoreCase(String activePrinciple);
    List<Product> findByIsPrescriptionRequired(Boolean isPrescriptionRequired);
}

