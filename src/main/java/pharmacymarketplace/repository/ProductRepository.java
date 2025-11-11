package pharmacymarketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByName(String name);

}
