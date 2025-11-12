package pharmacymarketplace.product.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pharmacymarketplace.domain.jpa.base.SoftDeletableEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// pharmacymarketplace/product/domain/jpa/Product.java
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_products_public_id", columnList = "public_id"),
        @Index(name = "idx_products_deleted_at", columnList = "deleted_at")
})
@Getter
@Setter
@SQLDelete(sql = "UPDATE products SET deleted_at = CURRENT_TIMESTAMP WHERE id =?")
@Where(clause = "deleted_at IS NULL")
public class Product extends SoftDeletableEntity {

    @Column(name = "public_id", columnDefinition = "BINARY(16)", nullable = false, unique = true)
    private UUID publicId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "anvisa_code", unique = true)
    private String anvisaCode;

    @Column(name = "active_principle", nullable = false)
    private String activePrinciple;

    @Column(name = "is_prescription_required", nullable = false)
    private boolean isPrescriptionRequired = false;

    //... outros campos...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductVariant> variants = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductCategory> productCategories = new HashSet<>();
}

