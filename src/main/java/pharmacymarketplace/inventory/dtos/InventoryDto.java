package pharmacymarketplace.inventory.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record InventoryDto(
        Long pharmacyId,
        String pharmacyName,
        Long productVariantId,
        String productVariantSku,
        String productName,
        BigDecimal price,
        Integer quantity,
        LocalDate expirationDate,
        Instant updatedAt
) {}

