package pharmacymarketplace.favorite.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.favorite.domain.jpa.Favorite;
import pharmacymarketplace.favorite.domain.jpa.Favorite.FavoriteType;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByCustomerId(Long customerId);
    List<Favorite> findByCustomerIdAndFavoriteType(Long customerId, FavoriteType type);
    Optional<Favorite> findByCustomerIdAndProductVariantId(Long customerId, Long productVariantId);
    Optional<Favorite> findByCustomerIdAndPharmacyId(Long customerId, Long pharmacyId);
    boolean existsByCustomerIdAndProductVariantId(Long customerId, Long productVariantId);
    boolean existsByCustomerIdAndPharmacyId(Long customerId, Long pharmacyId);
}

