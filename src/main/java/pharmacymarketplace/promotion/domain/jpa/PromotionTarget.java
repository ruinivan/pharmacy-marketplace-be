package pharmacymarketplace.promotion.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;
import pharmacymarketplace.product.domain.jpa.Category;
import pharmacymarketplace.product.domain.jpa.Product;
import pharmacymarketplace.product.domain.jpa.ProductVariant;

@Entity
@Table(name = "promotion_targets")
@Getter
@Setter
public class PromotionTarget extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;

    @Column(name = "target_id")
    private Long targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public enum TargetType {
        ALL_PRODUCTS,
        PRODUCT,
        PRODUCT_VARIANT,
        CATEGORY,
        BRAND
    }
}

