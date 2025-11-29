package pharmacymarketplace.user.services;

import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.user.domain.jpa.User;
import pharmacymarketplace.user.repository.jpa.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository repository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    public User createUser(User user){
        repository.findByEmail(user.getEmail()).ifPresent((userEmailFound) -> {
            throw new AlreadyExistsException("O email " + userEmailFound.getEmail() + " já existe!");
        });
        repository.findByPhoneNumber(user.getPhoneNumber()).ifPresent((userNumberFound) -> {
            throw new AlreadyExistsException("O telefone " + userNumberFound.getPhoneNumber() + " já existe!");
        });
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setHashedPassword(hashedPassword);
        return repository.save(user);
    }

    public List<User> findAllUser(){
        return repository.findAll();
    }

    public User findUserById(Long id){
        Optional<User> user = repository.findById(id);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("User not found!");
        }
        return user.get();
    }

    public User findUserByPublicId(UUID publicId){
        Optional<User> user = repository.findByPublicId(publicId);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("User not found!");
        }
        return user.get();
    }

    public User updateUserById(Long id, User user){
        Optional<User> userFound = repository.findById(id);
        if(userFound.isEmpty()){
            throw new ResourceNotFoundException("User not found!");
        }
        String hashedPassword = userFound.get().getHashedPassword();
        user.setHashedPassword(hashedPassword);
        return repository.save(user);
    }

    public User deleteUserById(Long id){
        Optional<User> userFound = repository.findById(id);
        if(userFound.isEmpty()){
            throw new ResourceNotFoundException("User not found!");
        }
        repository.deleteById(id);
        return userFound.get();
    }

    public User findUserByEmail(String email){
        Optional<User> user = repository.findByEmail(email);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("User not found!");
        }
        return user.get();
    }
}
