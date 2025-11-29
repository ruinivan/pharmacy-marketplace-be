package pharmacymarketplace.inventory;

import org.mapstruct.Mapper;
import pharmacymarketplace.inventory.domain.jpa.Inventory;
import pharmacymarketplace.inventory.dtos.InventoryDto;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    
    default InventoryDto toDto(Inventory inventory) {
        if (inventory == null) return null;
        
        Long pharmacyId = inventory.getPharmacy() != null ? inventory.getPharmacy().getId() : null;
        String pharmacyName = inventory.getPharmacy() != null ? inventory.getPharmacy().getTradeName() : null;
        
        Long productVariantId = inventory.getProductVariant() != null ? inventory.getProductVariant().getId() : null;
        String productVariantSku = inventory.getProductVariant() != null ? inventory.getProductVariant().getSku() : null;
        
        String productName = "Produto não disponível";
        try {
            if (inventory.getProductVariant() != null && 
                inventory.getProductVariant().getProduct() != null) {
                productName = inventory.getProductVariant().getProduct().getName();
            }
        } catch (Exception e) {
            // Produto não carregado (LAZY) ou null - mantém valor padrão
        }
        
        return new InventoryDto(
            pharmacyId,
            pharmacyName,
            productVariantId,
            productVariantSku,
            productName,
            inventory.getPrice(),
            inventory.getQuantity(),
            inventory.getExpirationDate(),
            inventory.getUpdatedAt()
        );
    }
}

