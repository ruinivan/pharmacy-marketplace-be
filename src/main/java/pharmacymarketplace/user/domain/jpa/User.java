package pharmacymarketplace.user.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // Importação crucial
import pharmacymarketplace.delivery.domain.jpa.DeliveryPersonnel;
import pharmacymarketplace.domain.jpa.base.SoftDeletableEntity;
import pharmacymarketplace.pharmacy.domain.jpa.PharmacyStaff;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_public_id", columnList = "public_id"),
        @Index(name = "idx_users_deleted_at", columnList = "deleted_at")
})
@Getter
@Setter
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id =?") // [3, 4]
@Where(clause = "deleted_at IS NULL") // [3, 4, 5]
// AQUI ESTÁ A CORREÇÃO PRINCIPAL:
public class User extends SoftDeletableEntity implements UserDetails {

    @Column(name = "public_id", columnDefinition = "BINARY(16)", nullable = false, unique = true) // [6, 7]
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

    @ManyToMany(fetch = FetchType.EAGER) // EAGER é necessário para carregar as roles para o UserDetails [8]
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

    // --- Implementação dos métodos da interface UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList()); // [8, 9]
    }

    @Override
    public String getPassword() {
        return this.hashedPassword;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Você pode adicionar lógica para isso se necessário
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Você pode adicionar lógica para isso se necessário
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Você pode adicionar lógica para isso se necessário
    }

    @Override
    public boolean isEnabled() {
        return this.isActive; // Usa a coluna is_active do seu banco
    }
}