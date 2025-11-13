package pharmacymarketplace.user.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@ToString
public class UserRole {
    @EmbeddedId
    private CustomerAddressId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    private Role role;
}
