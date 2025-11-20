package pharmacymarketplace.product.dtos;

import java.util.Set;
import java.util.UUID;

public record ProductDto(
        UUID publicId,
        String name,
        String description,
        String anvisaCode,
        String activePrinciple,
        Boolean isPrescriptionRequired,
        String controlledSubstanceList,
        Long brandId,
        String brandName,
        Long manufacturerId,
        String manufacturerName,
        Set<ProductVariantDto> variants,
        Set<Long> categoryIds
) {}

