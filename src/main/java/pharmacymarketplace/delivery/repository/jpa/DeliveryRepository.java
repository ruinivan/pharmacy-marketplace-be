package pharmacymarketplace.delivery.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.delivery.domain.jpa.Delivery;
import pharmacymarketplace.delivery.domain.jpa.Delivery.DeliveryStatus;
import pharmacymarketplace.order.domain.jpa.Order;
import pharmacymarketplace.user.domain.jpa.Address;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByOrder(Order order);
    Optional<Delivery> findByOrderId(Long orderId);
    Optional<Delivery> findByTrackingCode(String trackingCode);
    List<Delivery> findByDeliveryStatus(DeliveryStatus status);
    List<Delivery> findByDeliveryPersonnelId(Long deliveryPersonnelId);
    List<Delivery> findByAddress(Address address);
}

