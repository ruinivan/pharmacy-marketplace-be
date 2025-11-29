package pharmacymarketplace.cart.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.cart.dtos.AddCartItemRequest;
import pharmacymarketplace.cart.dtos.CartDto;
import pharmacymarketplace.cart.services.CartService;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.user.domain.jpa.User;
import pharmacymarketplace.user.repository.jpa.UserRepository;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_CUSTOMER')")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<CartDto> getCart(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        
        Long customerId = user.getCustomer().getId();
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItem(
            Authentication authentication,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        
        Long customerId = user.getCustomer().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(customerId, request));
    }

    @PutMapping("/items/{productVariantId}")
    public ResponseEntity<CartDto> updateItemQuantity(
            Authentication authentication,
            @PathVariable Long productVariantId,
            @RequestParam Integer quantity
    ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        
        Long customerId = user.getCustomer().getId();
        return ResponseEntity.ok(cartService.updateItemQuantity(customerId, productVariantId, quantity));
    }

    @DeleteMapping("/items/{productVariantId}")
    public ResponseEntity<Void> removeItem(
            Authentication authentication,
            @PathVariable Long productVariantId
    ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        
        Long customerId = user.getCustomer().getId();
        cartService.removeItem(customerId, productVariantId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        
        Long customerId = user.getCustomer().getId();
        cartService.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }
}

