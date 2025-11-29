package pharmacymarketplace.user.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.user.UserMapper;
import pharmacymarketplace.user.domain.jpa.User;
import pharmacymarketplace.user.dtos.UserDto;
import pharmacymarketplace.user.services.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = service.findAllUser().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        User user = service.findUserById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/public/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getUserByPublicId(@PathVariable UUID publicId) {
        User user = service.findUserByPublicId(publicId);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        User user = service.findUserByEmail(email);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> updateUserById(@PathVariable long id, @RequestBody User user) {
        User userUpdated = service.updateUserById(id, user);
        return ResponseEntity.ok(userMapper.toDto(userUpdated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUserById(@PathVariable long id) {
        service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}