package ru.peshekhonov.core.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.peshekhonov.core.entities.Product;

import java.math.BigDecimal;

public class ProductSpecifications {

    public static Specification<Product> priceGreaterThanOrEqualTo(BigDecimal price) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> priceLessThanOrEqualTo(BigDecimal price) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> titleLike(String titlePart) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), '%' + titlePart + '%');
    }

    public static Specification<Product> categoryTitleLike(String categoryTitle) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("category").get("title"), categoryTitle);
    }
}
