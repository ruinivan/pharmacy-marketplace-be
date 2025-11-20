package pharmacymarketplace.inventory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pharmacymarketplace.inventory.domain.jpa.Inventory;
import pharmacymarketplace.inventory.dtos.InventoryDto;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "pharmacyId", source = "pharmacy.id")
    @Mapping(target = "pharmacyName", source = "pharmacy.tradeName")
    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productVariantSku", source = "productVariant.sku")
    @Mapping(target = "productName", source = "productVariant.product.name")
    InventoryDto toDto(Inventory inventory);
}

