package pharmacymarketplace.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pharmacymarketplace.enums.OrderStatusEnum;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order extends Base{
    @Column(name = "public_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID publicId;

    @Column(name = "order_code", nullable = false)
    private String orderCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatusEnum status = OrderStatusEnum.PENDING;

    @Column(name = "subtotal_valor", nullable = false)
    private Double subtotalAmount;

    @Column(name = "discount_valor", nullable = false)
    private Double discountAmount;

    @Column(name = "shipping_fee", nullable = false)
    private Double shippingFee;

    @Column(name = "total_valor", nullable = false)
    private Double totalAmount;
}
