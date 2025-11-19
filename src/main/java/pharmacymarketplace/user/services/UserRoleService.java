package pharmacymarketplace.user.services;

import org.springframework.stereotype.Service;
import pharmacymarketplace.user.domain.jpa.UserRole;
import pharmacymarketplace.user.repository.jpa.UserRoleRepository;

@Service
public class UserRoleService {

    UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }
}
