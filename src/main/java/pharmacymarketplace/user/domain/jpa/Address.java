package pharmacymarketplace.user.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.base.BaseEntity;

// pharmacymarketplace/user/domain/jpa/Address.java
@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address extends BaseEntity {
    //... campos (street, complement, neighborhood, city, state, postalCode, country)
}