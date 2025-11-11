package pharmacymarketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
