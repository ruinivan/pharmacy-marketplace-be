package pharmacymarketplace.cart.domains.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// pharmacymarketplace/cart/domain/jpa/CartItemId.java
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class CartItemId implements Serializable {
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "product_variant_id")
    private Long productVariantId;
}