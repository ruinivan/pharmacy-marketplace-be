package pharmacymarketplace.product.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;
import pharmacymarketplace.domain.jpa.SoftDeletableEntity;

@Entity
@Table(name = "product_variants", indexes = @Index(name = "idx_product_variants_deleted_at", columnList = "deleted_at"))
@Getter
@Setter
@SQLDelete(sql = "UPDATE product_variants SET deleted_at = CURRENT_TIMESTAMP WHERE id =?")
@Filter(name = "deletedFilter")
public class ProductVariant extends SoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false)
    private String packageSize;

    @Column(unique = true)
    private String gtin;
}