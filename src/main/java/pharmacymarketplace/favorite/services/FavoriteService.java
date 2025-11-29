package pharmacymarketplace.favorite.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.favorite.FavoriteMapper;
import pharmacymarketplace.favorite.domain.jpa.Favorite;
import pharmacymarketplace.favorite.domain.jpa.Favorite.FavoriteType;
import pharmacymarketplace.favorite.dtos.AddFavoriteRequest;
import pharmacymarketplace.favorite.dtos.FavoriteDto;
import pharmacymarketplace.favorite.repository.jpa.FavoriteRepository;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.pharmacy.repository.jpa.PharmacyRepository;
import pharmacymarketplace.product.domain.jpa.ProductVariant;
import pharmacymarketplace.product.repository.jpa.ProductVariantRepository;
import pharmacymarketplace.user.domain.jpa.Customer;
import pharmacymarketplace.user.repository.jpa.CustomerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final CustomerRepository customerRepository;
    private final ProductVariantRepository productVariantRepository;
    private final PharmacyRepository pharmacyRepository;
    private final FavoriteMapper favoriteMapper;

    public List<FavoriteDto> getFavorites(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        
        return favoriteRepository.findByCustomerId(customerId).stream()
                .map(favoriteMapper::toDto)
                .toList();
    }

    public List<FavoriteDto> getFavoritesByType(Long customerId, FavoriteType type) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        
        return favoriteRepository.findByCustomerIdAndFavoriteType(customerId, type).stream()
                .map(favoriteMapper::toDto)
                .toList();
    }

    @Transactional
    public FavoriteDto addFavorite(Long customerId, AddFavoriteRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Favorite favorite = new Favorite();
        favorite.setCustomer(customer);
        favorite.setFavoriteType(request.favoriteType());

        if (request.favoriteType() == FavoriteType.PRODUCT) {
            if (request.productVariantId() == null) {
                throw new IllegalArgumentException("productVariantId é obrigatório para favoritos de produto");
            }
            if (favoriteRepository.existsByCustomerIdAndProductVariantId(customerId, request.productVariantId())) {
                throw new AlreadyExistsException("Produto já está nos favoritos");
            }
            ProductVariant variant = productVariantRepository.findById(request.productVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Variante do produto não encontrada"));
            favorite.setProductVariant(variant);
        } else if (request.favoriteType() == FavoriteType.PHARMACY) {
            if (request.pharmacyId() == null) {
                throw new IllegalArgumentException("pharmacyId é obrigatório para favoritos de farmácia");
            }
            if (favoriteRepository.existsByCustomerIdAndPharmacyId(customerId, request.pharmacyId())) {
                throw new AlreadyExistsException("Farmácia já está nos favoritos");
            }
            Pharmacy pharmacy = pharmacyRepository.findById(request.pharmacyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Farmácia não encontrada"));
            favorite.setPharmacy(pharmacy);
        }

        Favorite saved = favoriteRepository.save(favorite);
        return favoriteMapper.toDto(saved);
    }

    @Transactional
    public void removeFavorite(Long customerId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito não encontrado"));
        
        if (!favorite.getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException("Favorito não pertence ao cliente");
        }
        
        favoriteRepository.delete(favorite);
    }

    @Transactional
    public void removeProductFavorite(Long customerId, Long productVariantId) {
        Favorite favorite = favoriteRepository.findByCustomerIdAndProductVariantId(customerId, productVariantId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito não encontrado"));
        favoriteRepository.delete(favorite);
    }

    @Transactional
    public void removePharmacyFavorite(Long customerId, Long pharmacyId) {
        Favorite favorite = favoriteRepository.findByCustomerIdAndPharmacyId(customerId, pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito não encontrado"));
        favoriteRepository.delete(favorite);
    }
}

