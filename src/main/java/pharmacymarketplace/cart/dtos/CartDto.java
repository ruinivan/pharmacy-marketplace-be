package pharmacymarketplace.cart.dtos;

import java.util.List;

public record CartDto(
        Long customerId,
        List<CartItemDto> items
) {}

