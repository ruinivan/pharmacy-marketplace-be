package pharmacymarketplace.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.user.domain.jpa.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); // [16]
}