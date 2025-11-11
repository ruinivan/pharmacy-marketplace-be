package pharmacymarketplace.controller;

import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.model.Users;
import pharmacymarketplace.service.UserService; // Importe o Service

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service; // Injete o SERVICE, não o Repository


    @PostMapping
    public Users createUser(@RequestBody Users users) {
        return service.createUser(users);
    }

    @GetMapping
    public List<Users> listAllUsers() {
        return service.listAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<Users> getUserById(@PathVariable long id) {
        return service.findUserById(id);
    }

    @PutMapping("/{id}")
    public Optional<Users> updateUser(@PathVariable long id, @RequestBody Users user) {
        return service.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable long id) {
        return service.deleteUser(id);
    }
}