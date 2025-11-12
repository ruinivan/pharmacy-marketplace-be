package pharmacymarketplace.product.domains.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.base.BaseEntity;

// pharmacymarketplace/product/domain/jpa/Manufacturer.java
@Entity
@Table(name = "manufacturers")
@Getter
@Setter
public class Manufacturer extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
}