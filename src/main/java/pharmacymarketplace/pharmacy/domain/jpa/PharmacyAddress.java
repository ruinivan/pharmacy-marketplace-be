package pharmacymarketplace.pharmacy.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;
import pharmacymarketplace.user.domain.jpa.Address;

@Entity
@Table(name = "pharmacy_addresses")
@Getter
@Setter
public class PharmacyAddress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "opening_hours", columnDefinition = "TEXT")
    private String openingHours;
}

