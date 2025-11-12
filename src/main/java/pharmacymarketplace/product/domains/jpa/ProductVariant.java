package pharmacymarketplace.product.domains.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pharmacymarketplace.domain.jpa.base.SoftDeletableEntity;

// pharmacymarketplace/product/domain/jpa/ProductVariant.java
@Entity
@Table(name = "product_variants", indexes = @Index(name = "idx_product_variants_deleted_at", columnList = "deleted_at"))
@Getter
@Setter
@SQLDelete(sql = "UPDATE product_variants SET deleted_at = CURRENT_TIMESTAMP WHERE id =?")
@Where(clause = "deleted_at IS NULL")
public class ProductVariant extends SoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, unique = true)
    private String sku;

    private String dosage;
    private String packageSize;

    @Column(unique = true)
    private String gtin;
}