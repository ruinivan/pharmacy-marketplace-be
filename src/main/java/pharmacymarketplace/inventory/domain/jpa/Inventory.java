package pharmacymarketplace.inventory.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.product.domain.jpa.ProductVariant;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

// pharmacymarketplace/inventory/domain/jpa/Inventory.java
@Entity
@Table(name = "inventory")
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
    @JoinColumn(name = "productVariant_id", insertable = false, updatable = false)
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
