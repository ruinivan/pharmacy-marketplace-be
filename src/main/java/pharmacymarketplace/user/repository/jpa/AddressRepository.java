package pharmacymarketplace.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.user.domain.jpa.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

