package pharmacymarketplace.product.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record CreateProductRequest(
        @NotBlank(message = "Nome do produto é obrigatório")
        String name,

        String description,

        String anvisaCode,

        @NotBlank(message = "Princípio ativo é obrigatório")
        String activePrinciple,

        @NotNull(message = "Indicação de prescrição é obrigatória")
        Boolean isPrescriptionRequired,

        @NotBlank(message = "Lista de substâncias controladas é obrigatória")
        String controlledSubstanceList,

        Long brandId,

        Long manufacturerId,

        Set<CreateProductVariantRequest> variants,

        Set<Long> categoryIds
) {}

