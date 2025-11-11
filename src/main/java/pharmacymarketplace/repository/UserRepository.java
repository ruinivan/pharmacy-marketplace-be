package pharmacymarketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.model.Users;

import java.util.Optional;


public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
}