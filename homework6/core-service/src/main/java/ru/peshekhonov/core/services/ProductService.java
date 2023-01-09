package ru.peshekhonov.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.peshekhonov.core.entities.Product;
import ru.peshekhonov.core.events.ProductCreatedEvent;
import ru.peshekhonov.core.events.ProductDeletedEvent;
import ru.peshekhonov.core.identity.ProductIdentityMap;
import ru.peshekhonov.core.repositories.ProductRepository;
import ru.peshekhonov.core.repositories.specifications.ProductSpecifications;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "productCache")
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher publisher;

    @Cacheable(key = "#categoryTitle != null ? 'page'.concat(#page).concat('-').concat(#categoryTitle) : 'page_'.concat(#page)",
            condition = "#minPrice == null and #maxPrice == null and (#titlePart == null or #titlePart.isEmpty())")
    public Page<Product> findAll(BigDecimal minPrice, BigDecimal maxPrice, String titlePart, String categoryTitle, Integer page) {
        Specification<Product> spec = Specification.where(null);
        if (minPrice != null) {
            spec = spec.and(ProductSpecifications.priceGreaterThanOrEqualTo(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpecifications.priceLessThanOrEqualTo(maxPrice));
        }
        if (titlePart != null) {
            spec = spec.and(ProductSpecifications.titleLike(titlePart));
        }
        if (categoryTitle != null) {
            spec = spec.and(ProductSpecifications.categoryTitleLike(categoryTitle));
        }

        return productRepository.findAll(spec, PageRequest.of(page - 1, 5));
    }

    @CacheEvict(allEntries = true)
    public void deleteById(Long id) {
        Product product = null;
        Optional<Product> optional = findById(id);
        if (optional.isPresent()) {
            product = optional.get();
        }
        productRepository.deleteById(id);
        ProductIdentityMap.delete(id);
        if (product != null) {
            publisher.publishEvent(new ProductDeletedEvent(product));
        }
    }

    @CacheEvict(allEntries = true)
    public void createNewProduct(Product product) {
        product = productRepository.save(product);
        ProductIdentityMap.add(product);
        publisher.publishEvent(new ProductCreatedEvent(product));
    }

    public Optional<Product> findById(Long id) {
        if (ProductIdentityMap.contains(id)) {
            return Optional.of(ProductIdentityMap.get(id));
        }
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(ProductIdentityMap::add);
        return product;
    }
}
