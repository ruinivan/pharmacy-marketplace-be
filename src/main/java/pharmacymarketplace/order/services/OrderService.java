package pharmacymarketplace.order.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.inventory.domain.jpa.Inventory;
import pharmacymarketplace.inventory.repository.jpa.InventoryRepository;
import pharmacymarketplace.order.domain.jpa.*;
import pharmacymarketplace.order.dtos.CreateOrderRequest;
import pharmacymarketplace.order.enums.OrderStatusEnum;
import pharmacymarketplace.order.repository.jpa.OrderRepository;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.pharmacy.repository.jpa.PharmacyRepository;
import pharmacymarketplace.product.domain.jpa.ProductVariant;
import pharmacymarketplace.product.repository.jpa.ProductVariantRepository;
import pharmacymarketplace.user.domain.jpa.Customer;
import pharmacymarketplace.user.repository.jpa.CustomerRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final PharmacyRepository pharmacyRepository;
    private final ProductVariantRepository productVariantRepository;
    private final InventoryRepository inventoryRepository;

    public Order findByPublicId(UUID publicId) {
        return orderRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
    }

    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> findByPharmacyId(Long pharmacyId) {
        return orderRepository.findByPharmacyId(pharmacyId);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order createOrder(Long customerId, CreateOrderRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Pharmacy pharmacy = pharmacyRepository.findById(request.pharmacyId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmácia não encontrada"));

        Order order = new Order();
        order.setPublicId(UUID.randomUUID());
        order.setOrderCode(generateOrderCode());
        order.setCustomer(customer);
        order.setPharmacy(pharmacy);
        order.setOrderStatus(OrderStatusEnum.PENDING);

        BigDecimal subtotal = BigDecimal.ZERO;
        Set<OrderItem> items = new HashSet<>();

        for (var itemRequest : request.items()) {
            ProductVariant variant = productVariantRepository.findById(itemRequest.productVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Variante do produto não encontrada"));

            Inventory inventory = inventoryRepository
                    .findByPharmacyIdAndProductVariantId(pharmacy.getId(), variant.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não disponível no estoque"));

            if (inventory.getQuantity() < itemRequest.quantity()) {
                throw new IllegalStateException("Quantidade insuficiente em estoque");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductVariant(variant);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(inventory.getPrice());
            items.add(item);

            subtotal = subtotal.add(inventory.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
        }

        order.setItems(items);
        order.setSubtotal(subtotal);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setShippingCost(BigDecimal.ZERO);
        order.setTotal(subtotal);
        order.setNotes(request.notes());

        if (request.prescription() != null) {
            Prescription prescription = new Prescription();
            prescription.setOrder(order);
            prescription.setPrescriptionNumber(request.prescription().prescriptionNumber());
            prescription.setDoctorName(request.prescription().doctorName());
            prescription.setDoctorCrm(request.prescription().doctorCrm());
            prescription.setFileUrl(request.prescription().fileUrl());
            prescription.setNotes(request.prescription().notes());
            prescription.setPrescriptionDate(Instant.now());
            order.setPrescription(prescription);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(UUID publicId, OrderStatusEnum status) {
        Order order = findByPublicId(publicId);
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    private String generateOrderCode() {
        return "ORD-" + System.currentTimeMillis();
    }
}

