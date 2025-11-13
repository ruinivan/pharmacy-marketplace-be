package pharmacymarketplace.pharmacy.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.user.domain.jpa.User;

// pharmacymarketplace/pharmacy/domain/jpa/PharmacyStaff.java
@Entity
@Table(name = "pharmacy_staff", indexes = @Index(name = "idx_pharmacy_staff_pharmacy_id", columnList = "pharmacy_id"))
@Getter
@Setter
public class PharmacyStaff {

    @EmbeddedId
    private PharmacyStaffId id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pharmacyId")
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @Column(nullable = false)
    private String position;

}

