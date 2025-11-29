package pharmacymarketplace.delivery.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.delivery.domain.jpa.Delivery.DeliveryStatus;
import pharmacymarketplace.delivery.dtos.DeliveryDto;
import pharmacymarketplace.delivery.dtos.UpdateDeliveryRequest;
import pharmacymarketplace.delivery.services.DeliveryService;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryDto> getDeliveryByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(deliveryService.findByOrderId(orderId));
    }

    @GetMapping("/tracking/{trackingCode}")
    public ResponseEntity<DeliveryDto> getDeliveryByTrackingCode(@PathVariable String trackingCode) {
        return ResponseEntity.ok(deliveryService.findByTrackingCode(trackingCode));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DELIVERY_PERSONNEL')")
    public ResponseEntity<List<DeliveryDto>> getAllDeliveries() {
        return ResponseEntity.ok(deliveryService.findAll());
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DELIVERY_PERSONNEL')")
    public ResponseEntity<List<DeliveryDto>> getDeliveriesByStatus(@PathVariable DeliveryStatus status) {
        return ResponseEntity.ok(deliveryService.findByStatus(status));
    }

    @PostMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_PHARMACY_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<DeliveryDto> createDelivery(
            @PathVariable Long orderId,
            @RequestParam Long addressId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deliveryService.createDelivery(orderId, addressId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DELIVERY_PERSONNEL')")
    public ResponseEntity<DeliveryDto> updateDelivery(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDeliveryRequest request
    ) {
        return ResponseEntity.ok(deliveryService.updateDelivery(id, request));
    }
}

