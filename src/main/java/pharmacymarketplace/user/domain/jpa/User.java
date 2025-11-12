package pharmacymarketplace.user.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pharmacymarketplace.domain.jpa.base.SoftDeletableEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_public_id", columnList = "public_id"),
        @Index(name = "idx_users_deleted_at", columnList = "deleted_at")
})
@Getter @Setter
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id =?") // [69, 70]
@Where(clause = "deleted_at IS NULL") //
public class User extends SoftDeletableEntity {

    @Column(name = "public_id", columnDefinition = "BINARY(16)", nullable = false, unique = true) // [74]
    private UUID publicId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // --- Relacionamentos ---

    @ManyToMany(fetch = FetchType.EAGER) // EAGER é necessário para o UserDetailsService
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Customer customer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PharmacyStaff pharmacyStaff;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DeliveryPersonnel deliveryPersonnel;
}
