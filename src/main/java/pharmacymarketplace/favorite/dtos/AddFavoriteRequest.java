package pharmacymarketplace.favorite.dtos;

import jakarta.validation.constraints.NotNull;
import pharmacymarketplace.favorite.domain.jpa.Favorite.FavoriteType;

public record AddFavoriteRequest(
        @NotNull
        FavoriteType favoriteType,
        Long productVariantId,
        Long pharmacyId
) {}

