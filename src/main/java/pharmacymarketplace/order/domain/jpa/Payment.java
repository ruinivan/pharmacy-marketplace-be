package pharmacymarketplace.order.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public enum PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD,
        PIX,
        BANK_TRANSFER,
        CASH,
        OTHER
    }

    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        APPROVED,
        REJECTED,
        REFUNDED,
        CANCELLED
    }
}

