package pharmacymarketplace.inventory.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// pharmacymarketplace/inventory/domain/jpa/InventoryId.java
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class InventoryId implements Serializable {
    @Column(name = "pharmacy_id")
    private Long pharmacyId;
    @Column(name = "product_variant_id")
    private Long productVariantId;
}