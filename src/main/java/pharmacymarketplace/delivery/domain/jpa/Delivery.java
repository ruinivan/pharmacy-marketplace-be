package pharmacymarketplace.delivery.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.SoftDeletableEntity;
import pharmacymarketplace.order.domain.jpa.Order;
import pharmacymarketplace.user.domain.jpa.Address;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
public class Delivery extends SoftDeletableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_person_id")
    private DeliveryPersonnel deliveryPersonnel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;
}
