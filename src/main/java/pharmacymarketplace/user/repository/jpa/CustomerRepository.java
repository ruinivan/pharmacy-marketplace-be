package pharmacymarketplace.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.user.domain.jpa.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}