package pharmacymarketplace.user.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.user.domain.jpa.Role;
import pharmacymarketplace.user.services.RoleService;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ArrayList<Role>> getListRoles(){
        ArrayList<Role> roles = service.findAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id){
        Role role = service.findRoleById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        Role newRole = service.createRole(role);
        return new ResponseEntity<>(newRole, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role){
        Role updatedRole = service.updateRole(id, role);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id){
        service.deleteRole(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
