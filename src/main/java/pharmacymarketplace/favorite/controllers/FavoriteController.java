package pharmacymarketplace.favorite.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.favorite.domain.jpa.Favorite;
import pharmacymarketplace.favorite.dtos.AddFavoriteRequest;
import pharmacymarketplace.favorite.dtos.FavoriteDto;
import pharmacymarketplace.favorite.services.FavoriteService;
import pharmacymarketplace.user.domain.jpa.User;
import pharmacymarketplace.user.repository.jpa.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<FavoriteDto>> getFavorites(
            Authentication authentication
    ) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        Long customerId = user.getCustomer().getId();
        return ResponseEntity.ok(favoriteService.getFavorites(customerId));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<FavoriteDto>> getFavoritesByType(
            @PathVariable Favorite.FavoriteType type,
            Authentication authentication
    ) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        Long customerId = user.getCustomer().getId();
        return ResponseEntity.ok(favoriteService.getFavoritesByType(customerId, type));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<FavoriteDto> addFavorite(
            @Valid @RequestBody AddFavoriteRequest request,
            Authentication authentication
    ) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        Long customerId = user.getCustomer().getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteService.addFavorite(customerId, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long id,
            Authentication authentication
    ) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        Long customerId = user.getCustomer().getId();
        favoriteService.removeFavorite(customerId, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/product/{productVariantId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Void> removeProductFavorite(
            @PathVariable Long productVariantId,
            Authentication authentication
    ) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        Long customerId = user.getCustomer().getId();
        favoriteService.removeProductFavorite(customerId, productVariantId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/pharmacy/{pharmacyId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Void> removePharmacyFavorite(
            @PathVariable Long pharmacyId,
            Authentication authentication
    ) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        Long customerId = user.getCustomer().getId();
        favoriteService.removePharmacyFavorite(customerId, pharmacyId);
        return ResponseEntity.noContent().build();
    }
}

