package pharmacymarketplace.auth.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pharmacymarketplace.auth.JwtService;
import pharmacymarketplace.auth.dtos.AuthRequest;
import pharmacymarketplace.auth.dtos.AuthResponse;
import pharmacymarketplace.auth.dtos.CreateUserRequest;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.user.UserMapper;
import pharmacymarketplace.user.domain.jpa.Customer;
import pharmacymarketplace.user.domain.jpa.Role;
import pharmacymarketplace.user.domain.jpa.User;
import pharmacymarketplace.user.enums.CustomerTypeEnum;
import pharmacymarketplace.user.repository.jpa.CustomerRepository;
import pharmacymarketplace.user.repository.jpa.RoleRepository;
import pharmacymarketplace.user.repository.jpa.UserRepository;

import java.util.Set;
import java.util.UUID;

//...auth/AuthService.java
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository; // Assumindo que existe
    private final RoleRepository roleRepository; // Assumindo que existe
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(CreateUserRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new AlreadyExistsException("Email já cadastrado: " + request.email());
        }

        User user = new User();
        user.setPublicId(UUID.randomUUID());
        user.setEmail(request.email());
        user.setHashedPassword(passwordEncoder.encode(request.password()));
        user.setPhoneNumber(request.phoneNumber());
        user.setActive(true);

        Role userRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new ResourceNotFoundException("Role 'ROLE_CUSTOMER' não encontrada."));
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);

        // Cria o perfil de Customer associado
        Customer customer = new Customer();
        customer.setUser(savedUser); // Associa o usuário
        customer.setId(savedUser.getId()); // Define o mesmo ID
        customer.setFullName(request.fullName());
        customer.setCustomerType(CustomerTypeEnum.INDIVIDUAL); // Define um padrão ou extrai do request
        //... definir CPF/CNPJ se aplicável
        customerRepository.save(customer);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getHashedPassword(), user.getAuthorities()
        );

        String jwtToken = jwtService.generateToken(userDetails);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(AuthRequest request) {
        // Autentica [87, 88]
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Se bem-sucedido, gera o token
        var userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(userDetails);
        return new AuthResponse(jwtToken);
    }
}