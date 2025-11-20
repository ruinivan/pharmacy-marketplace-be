package pharmacymarketplace.pharmacy.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;

import java.util.List;
import java.util.Optional;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    Optional<Pharmacy> findByCnpj(String cnpj);
    Optional<Pharmacy> findByLegalName(String legalName);
    List<Pharmacy> findByTradeNameContainingIgnoreCase(String tradeName);
    List<Pharmacy> findByEmail(String email);
}

