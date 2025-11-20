package pharmacymarketplace.promotion.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;

@Entity
@Table(name = "promotion_rules")
@Getter
@Setter
public class PromotionRule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false)
    private RuleType ruleType;

    @Column(name = "rule_value", columnDefinition = "TEXT")
    private String ruleValue;

    @Column(name = "min_purchase_amount", precision = 10, scale = 2)
    private java.math.BigDecimal minPurchaseAmount;

    @Column(name = "min_quantity")
    private Integer minQuantity;

    public enum RuleType {
        MIN_PURCHASE_AMOUNT,
        MIN_QUANTITY,
        PRODUCT_CATEGORY,
        PRODUCT_BRAND,
        CUSTOMER_TYPE,
        FIRST_PURCHASE
    }
}
