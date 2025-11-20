package pharmacymarketplace.delivery.dtos;

import pharmacymarketplace.delivery.domain.jpa.Delivery.DeliveryStatus;

import java.time.Instant;

public record UpdateDeliveryRequest(
        Long deliveryPersonnelId,
        DeliveryStatus deliveryStatus,
        Instant estimatedDeliveryDate,
        Instant actualDeliveryDate,
        String trackingCode,
        String notes
) {}

