package pharmacymarketplace.order.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
        @NotNull(message = "ID da variante do produto é obrigatório")
        Long productVariantId,

        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 1, message = "Quantidade deve ser maior que zero")
        Integer quantity
) {}

