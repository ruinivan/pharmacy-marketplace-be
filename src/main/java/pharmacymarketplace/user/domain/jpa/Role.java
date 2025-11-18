package pharmacymarketplace.user.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;

// pharmacymarketplace/user/domain/jpa/Role.java
@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
}