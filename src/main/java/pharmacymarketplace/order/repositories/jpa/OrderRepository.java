//...order/repository/jpa/OrderRepository.java
package pharmacymarketplace.order.repositories.jpa;
import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.order.domains.jpa.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Métodos de consulta customizados (ex: findByCustomerId)
}
