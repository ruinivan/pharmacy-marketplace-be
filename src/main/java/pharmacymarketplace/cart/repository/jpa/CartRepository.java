package pharmacymarketplace.cart.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.cart.domain.jpa.CustomerCartItem;
import pharmacymarketplace.cart.domain.jpa.CartItemId;
import pharmacymarketplace.user.domain.jpa.Customer;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CustomerCartItem, CartItemId> {
    List<CustomerCartItem> findByCustomer(Customer customer);
    List<CustomerCartItem> findByCustomerId(Long customerId);
    Optional<CustomerCartItem> findByCustomerIdAndProductVariantId(Long customerId, Long productVariantId);
    void deleteByCustomerId(Long customerId);
}

