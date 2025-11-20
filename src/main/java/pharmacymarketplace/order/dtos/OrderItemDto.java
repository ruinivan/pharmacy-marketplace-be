package pharmacymarketplace.order.dtos;

import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        Long productVariantId,
        String productVariantSku,
        String productName,
        Integer quantity,
        BigDecimal unitPrice
) {}

