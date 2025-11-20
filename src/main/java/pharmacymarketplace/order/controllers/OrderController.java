package pharmacymarketplace.order.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<OrderDto>> getMyOrders(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Long customerId = user.getCustomer().getId();
        List<OrderDto> orders = orderService.findByCustomerId(customerId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{publicId}")
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
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Long customerId = user.getCustomer().getId();
        Order newOrder = orderService.createOrder(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toDto(newOrder));
    }

    @PatchMapping("/{publicId}/status")
    @PreAuthorize("hasAnyRole('ROLE_PHARMACY_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable UUID publicId,
            @RequestParam OrderStatusEnum status
    ) {
        Order updatedOrder = orderService.updateOrderStatus(publicId, status);
        return ResponseEntity.ok(orderMapper.toDto(updatedOrder));
    }
}

