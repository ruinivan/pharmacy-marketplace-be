package pharmacymarketplace.product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pharmacymarketplace.product.domain.jpa.Product;
import pharmacymarketplace.product.domain.jpa.ProductVariant;
import pharmacymarketplace.product.dtos.CreateProductRequest;
import pharmacymarketplace.product.dtos.CreateProductVariantRequest;
import pharmacymarketplace.product.dtos.ProductDto;
import pharmacymarketplace.product.dtos.ProductVariantDto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "manufacturer", ignore = true)
    @Mapping(target = "prescriptionRequired", source = "isPrescriptionRequired")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Product toEntity(CreateProductRequest request);

    @Mapping(target = "brandId", expression = "java(getBrandId(product))")
    @Mapping(target = "brandName", expression = "java(getBrandName(product))")
    @Mapping(target = "manufacturerId", expression = "java(getManufacturerId(product))")
    @Mapping(target = "manufacturerName", expression = "java(getManufacturerName(product))")
    @Mapping(target = "variants", expression = "java(mapVariants(product.getVariants()))")
    @Mapping(target = "categoryIds", expression = "java(mapCategoryIds(product.getProductCategories()))")
    @Mapping(target = "isPrescriptionRequired", source = "prescriptionRequired")
    ProductDto toDto(Product product);
    
    default Long getBrandId(Product product) {
        try {
            return product.getBrand() != null ? product.getBrand().getId() : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    default String getBrandName(Product product) {
        try {
            return product.getBrand() != null ? product.getBrand().getName() : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    default Long getManufacturerId(Product product) {
        try {
            return product.getManufacturer() != null ? product.getManufacturer().getId() : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    default String getManufacturerName(Product product) {
        try {
            return product.getManufacturer() != null ? product.getManufacturer().getName() : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ProductVariant toVariantEntity(CreateProductVariantRequest request);

    @Mapping(target = "productPublicId", expression = "java(getProductPublicId(variant))")
    ProductVariantDto toVariantDto(ProductVariant variant);
    
    default UUID getProductPublicId(ProductVariant variant) {
        try {
            return variant.getProduct() != null ? variant.getProduct().getPublicId() : null;
        } catch (Exception e) {
            return null;
        }
    }

    default Set<ProductVariantDto> mapVariants(Set<ProductVariant> variants) {
        if (variants == null) return null;
        try {
            return variants.stream()
                    .map(this::toVariantDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            // LazyInitializationException ou outra exceção - retorna conjunto vazio
            return new java.util.HashSet<>();
        }
    }

    default Set<Long> mapCategoryIds(Set<pharmacymarketplace.product.domain.jpa.ProductCategory> productCategories) {
        if (productCategories == null) return null;
        try {
            return productCategories.stream()
                    .map(pc -> {
                        try {
                            return pc.getCategory().getId();
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            // LazyInitializationException ou outra exceção - retorna conjunto vazio
            return new java.util.HashSet<>();
        }
    }
}

