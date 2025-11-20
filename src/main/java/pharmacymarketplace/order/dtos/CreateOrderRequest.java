package pharmacymarketplace.order.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record CreateOrderRequest(
        @NotNull(message = "ID da farmácia é obrigatório")
        Long pharmacyId,

        @NotEmpty(message = "O pedido deve conter pelo menos um item")
        @Valid
        Set<CreateOrderItemRequest> items,

        Long addressId,

        String notes,

        CreatePrescriptionRequest prescription
) {}

