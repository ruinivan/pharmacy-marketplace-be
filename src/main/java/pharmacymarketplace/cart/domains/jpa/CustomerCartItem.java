package pharmacymarketplace.cart.domains.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pharmacymarketplace.product.domains.jpa.ProductVariant;
import pharmacymarketplace.user.domains.jpa.Customer;

import java.time.Instant;

// pharmacymarketplace/cart/domain/jpa/CustomerCartItem.java
@Entity
@Table(name = "customer_cart_items")
@Getter
@Setter
public class CustomerCartItem {

    @EmbeddedId
    private CartItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerId")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productVariantId")
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer quantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}