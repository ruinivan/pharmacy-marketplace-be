package pharmacymarketplace.inventory.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.inventory.domain.jpa.Inventory;
import pharmacymarketplace.inventory.domain.jpa.InventoryId;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.product.domain.jpa.ProductVariant;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {
    List<Inventory> findByPharmacy(Pharmacy pharmacy);
    List<Inventory> findByPharmacyId(Long pharmacyId);
    List<Inventory> findByProductVariant(ProductVariant productVariant);
    List<Inventory> findByProductVariantId(Long productVariantId);
    Optional<Inventory> findByPharmacyIdAndProductVariantId(Long pharmacyId, Long productVariantId);
    List<Inventory> findByQuantityGreaterThan(Integer quantity);
}

