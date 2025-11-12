//...user/repository/jpa/UserRepository.java
package pharmacymarketplace.user.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.user.domains.jpa.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPublicId(UUID publicId);
}
