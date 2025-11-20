package pharmacymarketplace.pharmacy.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pharmacymarketplace.domain.jpa.SoftDeletableEntity;

// pharmacymarketplace/pharmacy/domain/jpa/Pharmacy.java
@Entity
@Table(name = "pharmacies", indexes = @Index(name = "idx_pharmacies_deleted_at", columnList = "deleted_at"))
@Getter
@Setter
@SQLDelete(sql = "UPDATE pharmacies SET deleted_at = CURRENT_TIMESTAMP WHERE id =?")
@Where(clause = "deleted_at IS NULL")
public class Pharmacy extends SoftDeletableEntity { // Estende SoftDeletableEntity, não Auditable

    @Column(name = "legal_name", nullable = false, unique = true)
    private String legalName;

    @Column(name = "trade_name", nullable = false)
    private String tradeName;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String phone;

    private String email;

    @Column(name = "website")
    private String website;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.Set<pharmacymarketplace.pharmacy.domain.jpa.PharmacyAddress> addresses = new java.util.HashSet<>();

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.Set<pharmacymarketplace.pharmacy.domain.jpa.PharmacyStaff> staff = new java.util.HashSet<>();
}

