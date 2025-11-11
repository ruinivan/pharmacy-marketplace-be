package pharmacymarketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacymarketplace.model.Users;
import pharmacymarketplace.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service // Diz ao Spring que esta é uma classe de serviço
public class UserService {

    @Autowired
    private UserRepository repository;

    public Users createUser(Users users) {
        return repository.save(users);
    }

    public List<Users> listAllUsers() {
        return repository.findAll();
    }

    public Optional<Users> findUserById(long id) {
        return repository.findById(id);
    }

    public Optional<Users> updateUser(long id, Users users) {
        return repository.findById(id)
                .map(userFound -> {
                    userFound.setNome(users.getNome());
                    userFound.setEmail(users.getEmail());
                    userFound.setSenha(users.getSenha());
                    return repository.save(userFound);
                });
    }

    public boolean deleteUser(long id) {
        return repository.findById(id)
                .map(userFound -> {
                    repository.delete(userFound);
                    return true;
                })
                .orElse(false);
    }

}