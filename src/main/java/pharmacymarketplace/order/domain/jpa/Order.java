package pharmacymarketplace.order.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.AuditableEntity;
import pharmacymarketplace.order.enums.OrderStatusEnum;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.user.domain.jpa.Customer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// pharmacymarketplace/order/domain/jpa/Order.java
@Entity
@Table(name = "orders", indexes = @Index(name = "idx_orders_public_id", columnList = "public_id"))
@Getter
@Setter
public class Order extends AuditableEntity { // Pedidos são auditáveis, mas não sofrem soft delete

    @Column(name = "public_id", columnDefinition = "BINARY(16)", nullable = false, unique = true)
    private UUID publicId;

    @Column(name = "order_code", unique = true)
    private String orderCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatusEnum orderStatus; // Enum

    //... campos de valores (subtotal, discount, shipping, total)...

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> items = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<Payment> payments = new HashSet<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Prescription prescription;
}
