package pharmacymarketplace.promotion.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;

import java.util.HashSet;
import java.util.Set;

// pharmacymarketplace/promotion/domain/jpa/Promotion.java
@Entity
@Table(name = "promotions")
@Getter
@Setter
public class Promotion extends BaseEntity {
    
    @Column(nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal discountValue;

    @Column(name = "start_date", nullable = false)
    private java.time.Instant startDate;

    @Column(name = "end_date", nullable = false)
    private java.time.Instant endDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "max_uses")
    private Integer maxUses;

    @Column(name = "current_uses")
    private Integer currentUses = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PromotionRule> rules = new HashSet<>();

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PromotionTarget> targets = new HashSet<>();

    public enum DiscountType {
        PERCENTAGE,
        FIXED_AMOUNT
    }
}