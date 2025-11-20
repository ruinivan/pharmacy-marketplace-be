package pharmacymarketplace.product.dtos;

import java.util.UUID;

public record ProductVariantDto(
        Long id,
        String sku,
        String dosage,
        String packageSize,
        String gtin,
        UUID productPublicId
) {}

