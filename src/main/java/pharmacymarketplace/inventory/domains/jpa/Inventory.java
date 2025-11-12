package pharmacymarketplace.inventory.domains.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import pharmacymarketplace.pharmacy.domains.jpa.Pharmacy;
import pharmacymarketplace.product.domains.jpa.ProductVariant;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

// pharmacymarketplace/inventory/domain/jpa/Inventory.java
@Entity
@Table(name = "inventory", indexes = @Index(name = "idx_inventory_product_variant_price", columnList = "product_variant_id, price"))
@Getter
@Setter
public class Inventory {

    @EmbeddedId
    private InventoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pharmacyId")
    private Pharmacy pharmacy;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productVariantId")
    private ProductVariant productVariant;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    private LocalDate expirationDate;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
