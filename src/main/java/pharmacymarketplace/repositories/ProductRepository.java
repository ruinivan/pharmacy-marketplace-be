package pharmacymarketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.models.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByName(String name);

}
