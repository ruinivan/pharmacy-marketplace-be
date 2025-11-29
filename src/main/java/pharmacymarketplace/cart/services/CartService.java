package pharmacymarketplace.cart.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pharmacymarketplace.cart.CartMapper;
import pharmacymarketplace.cart.domain.jpa.CartItemId;
import pharmacymarketplace.cart.domain.jpa.CustomerCartItem;
import pharmacymarketplace.cart.dtos.AddCartItemRequest;
import pharmacymarketplace.cart.dtos.CartDto;
import pharmacymarketplace.cart.repository.jpa.CartRepository;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.product.domain.jpa.ProductVariant;
import pharmacymarketplace.product.repository.jpa.ProductVariantRepository;
import pharmacymarketplace.user.domain.jpa.Customer;
import pharmacymarketplace.user.repository.jpa.CustomerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartMapper cartMapper;

    public CartDto getCart(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        List<CustomerCartItem> items = cartRepository.findByCustomerId(customerId);
        return cartMapper.toDto(customerId, items);
    }

    @Transactional
    public CartDto addItem(Long customerId, AddCartItemRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        ProductVariant variant = productVariantRepository.findById(request.productVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Variante do produto não encontrada"));

        CartItemId id = new CartItemId();
        id.setCustomerId(customerId);
        id.setProductVariantId(request.productVariantId());

        CustomerCartItem existingItem = cartRepository.findById(id).orElse(null);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
            cartRepository.save(existingItem);
        } else {
            CustomerCartItem item = new CustomerCartItem();
            item.setId(id);
            item.setCustomer(customer);
            item.setProductVariant(variant);
            item.setQuantity(request.quantity());
            cartRepository.save(item);
        }

        return getCart(customerId);
    }

    @Transactional
    public CartDto updateItemQuantity(Long customerId, Long productVariantId, Integer quantity) {
        CartItemId id = new CartItemId();
        id.setCustomerId(customerId);
        id.setProductVariantId(productVariantId);

        CustomerCartItem item = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho"));

        if (quantity <= 0) {
            cartRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartRepository.save(item);
        }

        return getCart(customerId);
    }

    @Transactional
    public void removeItem(Long customerId, Long productVariantId) {
        CartItemId id = new CartItemId();
        id.setCustomerId(customerId);
        id.setProductVariantId(productVariantId);

        CustomerCartItem item = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho"));

        cartRepository.delete(item);
    }

    @Transactional
    public void clearCart(Long customerId) {
        cartRepository.deleteByCustomerId(customerId);
    }
}

