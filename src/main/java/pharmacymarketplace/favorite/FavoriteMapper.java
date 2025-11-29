package pharmacymarketplace.favorite;

import org.mapstruct.Mapper;
import pharmacymarketplace.favorite.domain.jpa.Favorite;
import pharmacymarketplace.favorite.dtos.FavoriteDto;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    default FavoriteDto toDto(Favorite favorite) {
        if (favorite == null) return null;
        
        Long productVariantId = favorite.getProductVariant() != null ? favorite.getProductVariant().getId() : null;
        java.util.UUID productPublicId = favorite.getProductVariant() != null && favorite.getProductVariant().getProduct() != null 
            ? favorite.getProductVariant().getProduct().getPublicId() : null;
        String productName = favorite.getProductVariant() != null && favorite.getProductVariant().getProduct() != null
            ? favorite.getProductVariant().getProduct().getName() : null;
        
        Long pharmacyId = favorite.getPharmacy() != null ? favorite.getPharmacy().getId() : null;
        String pharmacyName = favorite.getPharmacy() != null ? favorite.getPharmacy().getTradeName() : null;
        
        return new FavoriteDto(
            favorite.getId(),
            favorite.getCustomer().getId(),
            productVariantId,
            productPublicId,
            productName,
            pharmacyId,
            pharmacyName,
            favorite.getFavoriteType()
        );
    }
}

