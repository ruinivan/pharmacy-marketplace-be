package pharmacymarketplace.delivery.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.delivery.domain.jpa.DeliveryPersonnel;

import java.util.Optional;

public interface DeliveryPersonnelRepository extends JpaRepository<DeliveryPersonnel, Long> {
    Optional<DeliveryPersonnel> findByUserId(Long userId);
}

