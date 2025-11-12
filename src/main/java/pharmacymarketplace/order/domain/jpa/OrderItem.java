package pharmacymarketplace.order.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.base.BaseEntity;
import pharmacymarketplace.product.domain.jpa.ProductVariant;

import java.math.BigDecimal;

// pharmacymarketplace/order/domain/jpa/OrderItem.java
@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;
}