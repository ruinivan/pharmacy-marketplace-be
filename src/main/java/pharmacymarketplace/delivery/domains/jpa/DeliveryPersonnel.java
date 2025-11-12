package pharmacymarketplace.delivery.domains.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.user.domains.jpa.User;

// pharmacymarketplace/delivery/domain/jpa/DeliveryPersonnel.java
@Entity
@Table(name = "delivery_personnel")
@Getter
@Setter
public class DeliveryPersonnel {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String cnh;

    @Column(columnDefinition = "TEXT")
    private String vehicleDetails;

    //... relacionamentos (deliveries)...
}