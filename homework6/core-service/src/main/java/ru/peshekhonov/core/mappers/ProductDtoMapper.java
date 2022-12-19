package ru.peshekhonov.core.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.peshekhonov.api.dto.ProductDto;
import ru.peshekhonov.api.exceptions.ResourceNotFoundException;
import ru.peshekhonov.core.entities.Category;
import ru.peshekhonov.core.entities.Product;
import ru.peshekhonov.core.services.CategoryService;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    @Mapping(source = "categoryTitle", target = "category", qualifiedByName = "getCategoryByTitle")
    Product map(ProductDto productDto, @Context CategoryService categoryService);

    @Named("getCategoryByTitle")
    default Category getCategoryByTitle(String categoryTitle, @Context CategoryService categoryService) {
        return categoryService.findByTitle(categoryTitle)
                .orElseThrow(() -> new ResourceNotFoundException("Категория с названием: " + categoryTitle + " не найдена"));
    }

    @Mapping(source = "category", target = "categoryTitle", qualifiedByName = "getCategoryTitle")
    ProductDto map(Product product);

    @Named("getCategoryTitle")
    default String getCategoryTitle(Category category) {
        return category.getTitle();
    }

    List<ProductDto> map(List<Product> products);
}
