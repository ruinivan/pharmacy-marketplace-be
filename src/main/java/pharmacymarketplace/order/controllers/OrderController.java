package pharmacymarketplace.order.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.order.OrderMapper;
import pharmacymarketplace.order.domain.jpa.Order;
import pharmacymarketplace.order.dtos.CreateOrderRequest;
import pharmacymarketplace.order.dtos.OrderDto;
import pharmacymarketplace.order.enums.OrderStatusEnum;
import pharmacymarketplace.order.services.OrderService;
import pharmacymarketplace.user.domain.jpa.User;
import pharmacymarketplace.user.repository.jpa.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrders(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        
        // Admin pode ver todos os pedidos
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            List<OrderDto> allOrders = orderService.findAll().stream()
                    .map(orderMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(allOrders);
        }
        
        // Cliente vê apenas seus pedidos
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        
        Long customerId = user.getCustomer().getId();
        List<OrderDto> orders = orderService.findByCustomerId(customerId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    @PreAuthorize("hasRole('ROLE_PHARMACY_ADMIN')")
    public ResponseEntity<List<OrderDto>> getPharmacyOrders(@PathVariable Long pharmacyId) {
        List<OrderDto> orders = orderService.findByPharmacyId(pharmacyId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{publicId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> getOrderByPublicId(@PathVariable UUID publicId) {
        Order order = orderService.findByPublicId(publicId);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<OrderDto> createOrder(
            Authentication authentication,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        
        if (user.getCustomer() == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para este usuário");
        }
        
        Long customerId = user.getCustomer().getId();
        Order newOrder = orderService.createOrder(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toDto(newOrder));
    }

    @PatchMapping("/{publicId}/status")
    @PreAuthorize("hasAnyRole('ROLE_PHARMACY_ADMIN', 'ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<OrderDto> updateOrderStatus(
            Authentication authentication,
            @PathVariable UUID publicId,
            @RequestParam OrderStatusEnum status
    ) {
        Order order = orderService.findByPublicId(publicId);
        
        // Cliente só pode cancelar seus próprios pedidos pendentes
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
            
            if (user.getCustomer() == null || !order.getCustomer().getId().equals(user.getCustomer().getId())) {
                throw new AccessDeniedException("Você não tem permissão para alterar este pedido");
            }
            
            // Cliente só pode cancelar pedidos pendentes
            if (status != OrderStatusEnum.CANCELLED || order.getOrderStatus() != OrderStatusEnum.PENDING) {
                throw new IllegalStateException("Cliente só pode cancelar pedidos pendentes");
            }
        }
        
        Order updatedOrder = orderService.updateOrderStatus(publicId, status);
        return ResponseEntity.ok(orderMapper.toDto(updatedOrder));
    }
}

