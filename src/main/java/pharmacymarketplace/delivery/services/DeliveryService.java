package pharmacymarketplace.delivery.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pharmacymarketplace.delivery.DeliveryMapper;
import pharmacymarketplace.delivery.domain.jpa.Delivery;
import pharmacymarketplace.delivery.domain.jpa.Delivery.DeliveryStatus;
import pharmacymarketplace.delivery.domain.jpa.DeliveryPersonnel;
import pharmacymarketplace.delivery.dtos.DeliveryDto;
import pharmacymarketplace.delivery.dtos.UpdateDeliveryRequest;
import pharmacymarketplace.delivery.repository.jpa.DeliveryRepository;
import pharmacymarketplace.delivery.repository.jpa.DeliveryPersonnelRepository;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.order.domain.jpa.Order;
import pharmacymarketplace.order.repository.jpa.OrderRepository;
import pharmacymarketplace.user.domain.jpa.Address;
import pharmacymarketplace.user.repository.jpa.AddressRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final DeliveryPersonnelRepository deliveryPersonnelRepository;
    private final DeliveryMapper deliveryMapper;

    public DeliveryDto findByOrderId(Long orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega não encontrada"));
        return deliveryMapper.toDto(delivery);
    }

    public DeliveryDto findByTrackingCode(String trackingCode) {
        Delivery delivery = deliveryRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega não encontrada"));
        return deliveryMapper.toDto(delivery);
    }

    public List<DeliveryDto> findAll() {
        return deliveryRepository.findAll().stream()
                .map(deliveryMapper::toDto)
                .toList();
    }

    public List<DeliveryDto> findByStatus(DeliveryStatus status) {
        return deliveryRepository.findByDeliveryStatus(status).stream()
                .map(deliveryMapper::toDto)
                .toList();
    }

    @Transactional
    public DeliveryDto createDelivery(Long orderId, Long addressId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        if (deliveryRepository.findByOrderId(orderId).isPresent()) {
            throw new IllegalStateException("Já existe uma entrega para este pedido");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setAddress(address);
        delivery.setDeliveryStatus(DeliveryStatus.PENDING);
        delivery.setTrackingCode(generateTrackingCode());

        Delivery saved = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(saved);
    }

    @Transactional
    public DeliveryDto updateDelivery(Long id, UpdateDeliveryRequest request) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega não encontrada"));

        if (request.deliveryPersonnelId() != null) {
            DeliveryPersonnel deliveryPersonnel = deliveryPersonnelRepository.findById(request.deliveryPersonnelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Entregador não encontrado"));
            delivery.setDeliveryPersonnel(deliveryPersonnel);
        }

        if (request.deliveryStatus() != null) {
            delivery.setDeliveryStatus(request.deliveryStatus());
            if (request.deliveryStatus() == DeliveryStatus.DELIVERED) {
                delivery.setActualDeliveryDate(Instant.now());
            }
        }

        if (request.estimatedDeliveryDate() != null) {
            delivery.setEstimatedDeliveryDate(request.estimatedDeliveryDate());
        }

        if (request.trackingCode() != null) {
            delivery.setTrackingCode(request.trackingCode());
        }

        if (request.notes() != null) {
            delivery.setNotes(request.notes());
        }

        return deliveryMapper.toDto(deliveryRepository.save(delivery));
    }

    private String generateTrackingCode() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

