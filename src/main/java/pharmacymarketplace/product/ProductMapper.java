package pharmacymarketplace.product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pharmacymarketplace.product.domain.jpa.Product;
import pharmacymarketplace.product.domain.jpa.ProductVariant;
import pharmacymarketplace.product.dtos.CreateProductRequest;
import pharmacymarketplace.product.dtos.CreateProductVariantRequest;
import pharmacymarketplace.product.dtos.ProductDto;
import pharmacymarketplace.product.dtos.ProductVariantDto;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "manufacturer", ignore = true)
    @Mapping(target = "isPrescriptionRequired", source = "isPrescriptionRequired")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Product toEntity(CreateProductRequest request);

    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "brandName", source = "brand.name")
    @Mapping(target = "manufacturerId", source = "manufacturer.id")
    @Mapping(target = "manufacturerName", source = "manufacturer.name")
    @Mapping(target = "variants", expression = "java(mapVariants(product.getVariants()))")
    @Mapping(target = "categoryIds", expression = "java(mapCategoryIds(product.getProductCategories()))")
    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ProductVariant toVariantEntity(CreateProductVariantRequest request);

    @Mapping(target = "productPublicId", source = "product.publicId")
    ProductVariantDto toVariantDto(ProductVariant variant);

    default Set<ProductVariantDto> mapVariants(Set<ProductVariant> variants) {
        if (variants == null) return null;
        return variants.stream()
                .map(this::toVariantDto)
                .collect(Collectors.toSet());
    }

    default Set<Long> mapCategoryIds(Set<pharmacymarketplace.product.domain.jpa.ProductCategory> productCategories) {
        if (productCategories == null) return null;
        return productCategories.stream()
                .map(pc -> pc.getCategory().getId())
                .collect(Collectors.toSet());
    }
}

