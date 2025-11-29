package pharmacymarketplace.user.services;

import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.user.domain.jpa.Role;
import pharmacymarketplace.user.repository.jpa.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> findAllRoles(){
        return repository.findAll();
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

    public Role updateRole(Long id, Role role){
        Role existingRole = findRoleById(id);
        if(!existingRole.getName().equals(role.getName()) && repository.findByName(role.getName()).isPresent()){
            throw new AlreadyExistsException("Role name already exists");
        }
        existingRole.setName(role.getName());
        return repository.save(existingRole);
    }

    public void deleteRole(Long id){
        Role role = findRoleById(id);
        repository.delete(role);
    }
}
