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
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_person_id")
    private DeliveryPersonnel deliveryPersonnel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    @Column(name = "estimated_delivery_date")
    private java.time.Instant estimatedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private java.time.Instant actualDeliveryDate;

    @Column(name = "tracking_code", unique = true)
    private String trackingCode;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public enum DeliveryStatus {
        PENDING,
        ASSIGNED,
        IN_TRANSIT,
        OUT_FOR_DELIVERY,
        DELIVERED,
        FAILED,
        RETURNED,
        CANCELLED
    }
}
