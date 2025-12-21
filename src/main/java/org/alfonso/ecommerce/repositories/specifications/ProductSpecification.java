package org.alfonso.ecommerce.repositories.specifications;

import jakarta.persistence.criteria.Join;
import org.alfonso.ecommerce.entities.Brand;
import org.alfonso.ecommerce.entities.Category;
import org.alfonso.ecommerce.entities.Product;
import org.alfonso.ecommerce.entities.ProductVariant;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {
    public static Specification<Product> filter(
            String categorySlug,
            List<String> brandIds,
            Double minPrice,
            Double maxPrice
    ) {
        return (root, query, cb) -> {

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (categorySlug != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(
                        cb.equal(categoryJoin.get("slug"), categorySlug)
                );
            }

            if (brandIds != null && !brandIds.isEmpty()) {
                Join<Product, Brand> brandJoin = root.join("brand");
                predicates.add(
                        brandJoin.get("id").in(brandIds)
                );
            }

            if (minPrice != null || maxPrice != null) {
                Join<Product, ProductVariant> variantJoin =
                        root.join("variants");

                if (minPrice != null) {
                    predicates.add(
                            cb.greaterThanOrEqualTo(
                                    variantJoin.get("price"),
                                    minPrice
                            )
                    );
                }

                if (maxPrice != null) {
                    predicates.add(
                            cb.lessThanOrEqualTo(
                                    variantJoin.get("price"),
                                    maxPrice
                            )
                    );
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
