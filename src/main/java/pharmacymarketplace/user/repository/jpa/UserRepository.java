//...user/repository/jpa/UserRepository.java
package pharmacymarketplace.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacymarketplace.user.domain.jpa.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.customer WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByPublicId(UUID publicId);
}
