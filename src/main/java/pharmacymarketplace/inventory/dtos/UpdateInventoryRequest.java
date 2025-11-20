package pharmacymarketplace.inventory.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateInventoryRequest(
        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.0", message = "Preço deve ser maior ou igual a zero")
        BigDecimal price,

        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 0, message = "Quantidade deve ser maior ou igual a zero")
        Integer quantity,

        LocalDate expirationDate
) {}

