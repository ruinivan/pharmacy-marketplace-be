package pharmacymarketplace.delivery.dtos;

import pharmacymarketplace.delivery.domain.jpa.Delivery.DeliveryStatus;

import java.time.Instant;

public record DeliveryDto(
        Long id,
        Long orderId,
        String orderCode,
        Long deliveryPersonnelId,
        String deliveryPersonnelName,
        Long addressId,
        String addressStreet,
        DeliveryStatus deliveryStatus,
        Instant estimatedDeliveryDate,
        Instant actualDeliveryDate,
        String trackingCode,
        String notes
) {}

