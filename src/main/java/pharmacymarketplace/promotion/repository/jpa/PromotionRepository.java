package pharmacymarketplace.promotion.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.promotion.domain.jpa.Promotion;

import java.time.Instant;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByPharmacy(Pharmacy pharmacy);
    List<Promotion> findByPharmacyId(Long pharmacyId);
    List<Promotion> findByIsActive(Boolean isActive);
    List<Promotion> findByPharmacyIdAndIsActive(Long pharmacyId, Boolean isActive);
    List<Promotion> findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActive(
            Instant now, Instant now2, Boolean isActive);
}

