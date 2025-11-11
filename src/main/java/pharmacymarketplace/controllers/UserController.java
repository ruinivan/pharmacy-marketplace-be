// Em: /controllers/UserController.java
package pharmacymarketplace.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.model.Users;
import pharmacymarketplace.services.UserService;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        Users newUser = service.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ArrayList<Users>> getAllUsers() {
        ArrayList<Users> users = service.listAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable long id) {
        Users user = service.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> putUser(@PathVariable long id, @RequestBody Users user) {
        Users userUpdated = service.updateUserById(id, user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long id) {
        service.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}