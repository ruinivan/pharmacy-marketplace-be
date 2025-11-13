package pharmacymarketplace.user.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;

// pharmacymarketplace/user/domain/jpa/Address.java
@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address extends BaseEntity {

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    private String complement;
}