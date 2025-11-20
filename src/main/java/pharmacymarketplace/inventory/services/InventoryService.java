package pharmacymarketplace.inventory.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.inventory.InventoryMapper;
import pharmacymarketplace.inventory.domain.jpa.Inventory;
import pharmacymarketplace.inventory.domain.jpa.InventoryId;
import pharmacymarketplace.inventory.dtos.InventoryDto;
import pharmacymarketplace.inventory.dtos.UpdateInventoryRequest;
import pharmacymarketplace.inventory.repository.jpa.InventoryRepository;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.pharmacy.repository.jpa.PharmacyRepository;
import pharmacymarketplace.product.domain.jpa.ProductVariant;
import pharmacymarketplace.product.repository.jpa.ProductVariantRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final PharmacyRepository pharmacyRepository;
    private final ProductVariantRepository productVariantRepository;
    private final InventoryMapper inventoryMapper;

    public List<InventoryDto> findByPharmacyId(Long pharmacyId) {
        List<Inventory> inventories = inventoryRepository.findByPharmacyId(pharmacyId);
        return inventories.stream()
                .map(inventoryMapper::toDto)
                .toList();
    }

    public List<InventoryDto> findByProductVariantId(Long productVariantId) {
        List<Inventory> inventories = inventoryRepository.findByProductVariantId(productVariantId);
        return inventories.stream()
                .map(inventoryMapper::toDto)
                .toList();
    }

    @Transactional
    public InventoryDto updateInventory(Long pharmacyId, Long productVariantId, UpdateInventoryRequest request) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmácia não encontrada"));

        ProductVariant variant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variante do produto não encontrada"));

        InventoryId id = new InventoryId();
        id.setPharmacyId(pharmacyId);
        id.setProductVariantId(productVariantId);

        Inventory inventory = inventoryRepository.findById(id).orElse(null);
        if (inventory == null) {
            inventory = new Inventory();
            inventory.setId(id);
            inventory.setPharmacy(pharmacy);
            inventory.setProductVariant(variant);
        }

        inventory.setPrice(request.price());
        inventory.setQuantity(request.quantity());
        inventory.setExpirationDate(request.expirationDate());

        Inventory saved = inventoryRepository.save(inventory);
        return inventoryMapper.toDto(saved);
    }

    @Transactional
    public void deleteInventory(Long pharmacyId, Long productVariantId) {
        InventoryId id = new InventoryId();
        id.setPharmacyId(pharmacyId);
        id.setProductVariantId(productVariantId);

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        inventoryRepository.delete(inventory);
    }
}

