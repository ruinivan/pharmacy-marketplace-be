package pharmacymarketplace.promotion.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "promotion_rules")
@Getter
@Setter
public class PromotionRule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

}
