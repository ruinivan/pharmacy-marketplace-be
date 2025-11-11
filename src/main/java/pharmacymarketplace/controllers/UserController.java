package pharmacymarketplace.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.model.Users;
import pharmacymarketplace.services.UserService; // Importe o Service

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service; // Injete o SERVICE, não o Repository


    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        try{
            Users newUser = service.createUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch(Exception e){
            System.err.println("Erro ao adicionar Usuário: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<ArrayList<Users>> listAllUsers() {
        try {
            ArrayList<Users> users = new ArrayList<>();
            users.addAll(service.listAllUsers());
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch(Exception e){
            System.err.println("Erro ao listar Usuários: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable long id) {
        try{
            Optional<Users> user = service.findUserById(id);
            if(user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception e){
            System.err.println("Erro ao buscar Usuário por Id: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable long id, @RequestBody Users user) {
        try{
            Optional<Users> userUpdated = service.updateUser(id, user);
            if(userUpdated.isPresent()) {
                return new ResponseEntity<>(userUpdated.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Users> deleteUser(@PathVariable long id) {
        try{
            Optional<Users> userDeleted = service.deleteUser(id);
            if (userDeleted.isPresent()) {
                return new ResponseEntity<>(userDeleted.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}