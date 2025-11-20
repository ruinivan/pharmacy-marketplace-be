package pharmacymarketplace.order.dtos;

import pharmacymarketplace.order.domain.jpa.Payment.PaymentMethod;
import pharmacymarketplace.order.domain.jpa.Payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentDto(
        Long id,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        BigDecimal amount,
        String transactionId,
        Instant paidAt,
        String notes
) {}

