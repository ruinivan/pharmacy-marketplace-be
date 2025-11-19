package pharmacymarketplace.user.services;

import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.user.domain.jpa.Role;
import pharmacymarketplace.user.domain.jpa.User;
import pharmacymarketplace.user.repository.jpa.RoleRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public ArrayList<Role> findAllRoles(){
        return new ArrayList<>(repository.findAll());
    }

    public Role findRoleById(Long id){
        Optional<Role> role = repository.findById(id);
        if(role.isEmpty()){
            throw new ResourceNotFoundException("Role not found");
        }
        return role.get();
    }

    public Role createRole(Role role){
        if(repository.findByName(role.getName()).isPresent()){
            throw new AlreadyExistsException("Role already exists");
        }
        return repository.save(role);
    }

    public Role saveRoleById(Long id, Role role){
        this.findRoleById(id);
        return repository.save(role);
    }

    public Role deleteRoleById(Long id){
        Role role = this.findRoleById(id);
        repository.deleteById(id);
        return role;
    }
}
