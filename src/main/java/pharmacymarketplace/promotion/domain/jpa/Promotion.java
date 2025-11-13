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
    //... campos (name, discountType, discountValue, start/end_date)...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    private Set<PromotionRule> rules = new HashSet<>();

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    private Set<PromotionTarget> targets = new HashSet<>();
}
//... (PromotionRule.java e PromotionTarget.java seguem o padrão)