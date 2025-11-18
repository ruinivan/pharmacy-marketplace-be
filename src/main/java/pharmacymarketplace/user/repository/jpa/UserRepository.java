//...user/repository/jpa/UserRepository.java
package pharmacymarketplace.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.user.domain.jpa.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phoneNumber);
    Optional<User> findByPublicId(UUID publicId);
}
