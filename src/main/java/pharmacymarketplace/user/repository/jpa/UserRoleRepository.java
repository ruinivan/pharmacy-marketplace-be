package pharmacymarketplace.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.user.domain.jpa.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
}
