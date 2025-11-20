package pharmacymarketplace.cart.dtos;

import java.time.Instant;

public record CartItemDto(
        Long productVariantId,
        String productVariantSku,
        String productName,
        String dosage,
        String packageSize,
        Integer quantity,
        Instant createdAt
) {}

