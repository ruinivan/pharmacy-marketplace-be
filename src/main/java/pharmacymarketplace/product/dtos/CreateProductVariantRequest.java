package pharmacymarketplace.product.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateProductVariantRequest(
        @NotBlank(message = "SKU é obrigatório")
        String sku,

        @NotBlank(message = "Dosagem é obrigatória")
        String dosage,

        @NotBlank(message = "Tamanho da embalagem é obrigatório")
        String packageSize,

        String gtin
) {}

