package pharmacymarketplace.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacymarketplace.model.Users;
import pharmacymarketplace.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service // Diz ao Spring que esta é uma classe de serviço
public class UserService {

    @Autowired
    private UserRepository repository;

    public Users createUser(Users user) {
        Optional<Users> userAlreadyCreated = repository.findByEmail(user.getEmail());

        if (userAlreadyCreated.isPresent()) {
            throw new RuntimeException("Usuário com email " + user.getEmail() + " já existe!");
        }

        return repository.save(user);
    }

    public ArrayList<Users> listAllUsers() {
        return (ArrayList<Users>) repository.findAll();
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

    public Optional<Users> deleteUser(long id) {
        Optional<Users> userToDelete = repository.findById(id);

        if (userToDelete.isPresent()) {
            repository.delete(userToDelete.get());
        }

        return userToDelete;
    }

}