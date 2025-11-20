package pharmacymarketplace.product.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.product.ProductMapper;
import pharmacymarketplace.product.domain.jpa.*;
import pharmacymarketplace.product.dtos.CreateProductRequest;
import pharmacymarketplace.product.dtos.CreateProductVariantRequest;
import pharmacymarketplace.product.repository.jpa.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ProductMapper productMapper;

    public Product findByPublicId(UUID publicId) {
        return productRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Product createProduct(CreateProductRequest request) {
        Product product = productMapper.toEntity(request);
        product.setPublicId(UUID.randomUUID());

        if (request.brandId() != null) {
            Brand brand = brandRepository.findById(request.brandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada"));
            product.setBrand(brand);
        }

        if (request.manufacturerId() != null) {
            Manufacturer manufacturer = manufacturerRepository.findById(request.manufacturerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado"));
            product.setManufacturer(manufacturer);
        }

        Product savedProduct = productRepository.save(product);

        // Criar variantes
        if (request.variants() != null && !request.variants().isEmpty()) {
            Set<ProductVariant> variants = new HashSet<>();
            for (CreateProductVariantRequest variantRequest : request.variants()) {
                if (productVariantRepository.findBySku(variantRequest.sku()).isPresent()) {
                    throw new AlreadyExistsException("SKU já existe: " + variantRequest.sku());
                }
                ProductVariant variant = productMapper.toVariantEntity(variantRequest);
                variant.setProduct(savedProduct);
                variants.add(productVariantRepository.save(variant));
            }
            savedProduct.setVariants(variants);
        }

        // Associar categorias
        if (request.categoryIds() != null && !request.categoryIds().isEmpty()) {
            Set<ProductCategory> productCategories = new HashSet<>();
            for (Long categoryId : request.categoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
                ProductCategoryId id = new ProductCategoryId();
                id.setProductId(savedProduct.getId());
                id.setCategoryId(categoryId);
                ProductCategory productCategory = new ProductCategory();
                productCategory.setId(id);
                productCategory.setProduct(savedProduct);
                productCategory.setCategory(category);
                productCategories.add(productCategory);
            }
            savedProduct.setProductCategories(productCategories);
        }

        return productRepository.save(savedProduct);
    }

    @Transactional
    public Product updateProduct(UUID publicId, CreateProductRequest request) {
        Product product = findByPublicId(publicId);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setAnvisaCode(request.anvisaCode());
        product.setActivePrinciple(request.activePrinciple());
        product.setIsPrescriptionRequired(request.isPrescriptionRequired());
        product.setControlledSubstanceList(request.controlledSubstanceList());

        if (request.brandId() != null) {
            Brand brand = brandRepository.findById(request.brandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada"));
            product.setBrand(brand);
        }

        if (request.manufacturerId() != null) {
            Manufacturer manufacturer = manufacturerRepository.findById(request.manufacturerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado"));
            product.setManufacturer(manufacturer);
        }

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(UUID publicId) {
        Product product = findByPublicId(publicId);
        productRepository.delete(product);
    }
}

