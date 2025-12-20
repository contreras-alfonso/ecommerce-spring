package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.AvailableFilters;
import org.alfonso.ecommerce.dto.ProductListItem;
import org.alfonso.ecommerce.dto.ProductFilters;
import org.alfonso.ecommerce.dto.ProductSearchResponse;
import org.alfonso.ecommerce.entities.Brand;
import org.alfonso.ecommerce.projection.FiltersListProjection;
import org.alfonso.ecommerce.projection.ProductListItemProjection;
import org.alfonso.ecommerce.repositories.ProductSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;

    public ProductSearchResponse search(String categorySlug, ProductFilters request) {
        // validación de sort
        String sort = Optional.ofNullable(request.getSort())
                .filter(s -> s.matches("price_asc|price_desc|created_asc"))
                .orElse("created_desc");

        // Crear Pageable
        Pageable pageable = PageRequest.of(
                Math.max(0, request.getPage()),
                Math.min(100, Math.max(1, request.getSize()))
        );

        // Buscar productos
        Page<ProductListItemProjection> productPage = productSearchRepository.findProducts(
                categorySlug,
                request.getBrandIds(),
                request.getMinPrice(),
                request.getMaxPrice(),
                sort,
                pageable
        );

        // Mapear a ProductListItem
        Page<ProductListItem> mappedPage = productPage.map(this::toProductListItem);

        // Obtener filtros disponibles
        AvailableFilters filters = getAvailableFilters(
                categorySlug,
                request.getBrandIds(),
                request.getMinPrice(),
                request.getMaxPrice()
        );

        return new ProductSearchResponse(mappedPage, filters);

    }

    private ProductListItem toProductListItem(ProductListItemProjection p) {
        return new ProductListItem(
                p.getId(),
                p.getName(),
                p.getSlug(),
                p.getDescription(),
                p.isUsesTechnicalVariants(),
                new Brand(p.getBrandId(), p.getBrandName(), p.getBrandSlug()),
                p.getMinPrice(),
                p.getMaxPrice(),
                p.getCreatedAt()
        );
    }

    private AvailableFilters getAvailableFilters(
            String categorySlug,
            List<String> brandIds,
            Double minPrice,
            Double maxPrice
    ) {
        List<FiltersListProjection> availableFilters = productSearchRepository.findAvailableFilters(categorySlug, brandIds, minPrice, maxPrice);

        if (availableFilters.isEmpty()) {
            return new AvailableFilters(null, null, List.of());
        }

        Double globalMin = availableFilters.stream()
                .map(FiltersListProjection::getMinPrice)
                .filter(Objects::nonNull)
                .min(Double::compareTo)
                .orElse(null);

        Double globalMax = availableFilters.stream()
                .map(FiltersListProjection::getMaxPrice)
                .filter(Objects::nonNull)
                .max(Double::compareTo)
                .orElse(null);

        List<Brand> brands = availableFilters.stream()
                .map(s -> new Brand(s.getBrandId(), s.getBrandName(), s.getBrandSlug()))
                .distinct()
                .collect(Collectors.toList());

        return new AvailableFilters(globalMin, globalMax, brands);
    }
}
