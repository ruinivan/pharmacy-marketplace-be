package pharmacymarketplace.favorite.dtos;

import pharmacymarketplace.favorite.domain.jpa.Favorite.FavoriteType;
import java.util.UUID;

public record FavoriteDto(
        Long id,
        Long customerId,
        Long productVariantId,
        UUID productPublicId,
        String productName,
        Long pharmacyId,
        String pharmacyName,
        FavoriteType favoriteType
) {}

