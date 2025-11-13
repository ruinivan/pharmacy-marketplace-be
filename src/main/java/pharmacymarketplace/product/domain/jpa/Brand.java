package pharmacymarketplace.product.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;

// pharmacymarketplace/product/domain/jpa/Brand.java
@Entity
@Table(name = "brands")
@Getter
@Setter
public class Brand extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
}
