package pharmacymarketplace.order.dtos;

import pharmacymarketplace.order.enums.OrderStatusEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record OrderDto(
        UUID publicId,
        String orderCode,
        Long customerId,
        Long pharmacyId,
        OrderStatusEnum orderStatus,
        BigDecimal subtotal,
        BigDecimal discountAmount,
        BigDecimal shippingCost,
        BigDecimal total,
        String notes,
        Set<OrderItemDto> items,
        Set<PaymentDto> payments,
        PrescriptionDto prescription,
        Instant createdAt,
        Instant updatedAt
) {}

