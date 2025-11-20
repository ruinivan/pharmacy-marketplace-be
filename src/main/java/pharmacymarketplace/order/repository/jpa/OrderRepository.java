package pharmacymarketplace.order.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.order.domain.jpa.Order;
import pharmacymarketplace.order.enums.OrderStatusEnum;
import pharmacymarketplace.user.domain.jpa.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByPublicId(UUID publicId);
    Optional<Order> findByOrderCode(String orderCode);
    List<Order> findByCustomer(Customer customer);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByOrderStatus(OrderStatusEnum status);
    List<Order> findByPharmacyId(Long pharmacyId);
}
