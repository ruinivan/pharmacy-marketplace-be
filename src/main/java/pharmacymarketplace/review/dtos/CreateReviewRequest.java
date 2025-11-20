package pharmacymarketplace.review.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import pharmacymarketplace.review.domain.mongo.ReviewDocument.ReviewableType;

public record CreateReviewRequest(
        @NotNull(message = "Avaliação é obrigatória")
        @Min(value = 1, message = "Avaliação deve ser entre 1 e 5")
        @Max(value = 5, message = "Avaliação deve ser entre 1 e 5")
        int rating,

        String comment,

        @NotNull(message = "Tipo de item avaliável é obrigatório")
        ReviewableType reviewableType,

        @NotNull(message = "ID do item avaliável é obrigatório")
        Long reviewableId
) {}

