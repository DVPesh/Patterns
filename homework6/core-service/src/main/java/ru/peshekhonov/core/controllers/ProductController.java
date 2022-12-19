package ru.peshekhonov.core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.peshekhonov.api.dto.ProductDto;
import ru.peshekhonov.api.exceptions.AppError;
import ru.peshekhonov.api.exceptions.ResourceNotFoundException;
import ru.peshekhonov.core.mappers.ProductDtoMapper;
import ru.peshekhonov.core.services.CategoryService;
import ru.peshekhonov.core.services.ProductService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Товары", description = "Методы работы с товарами")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductDtoMapper productDtoMapper;

    @Operation(
            summary = "Запрос на получение страницы товаров",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    )
            }
    )
    @GetMapping
    public Page<ProductDto> getAllProducts(
            @RequestParam(name = "page", defaultValue = "1") @Parameter(description = "Номер страницы", required = false) Integer page,
            @RequestParam(name = "min_price", required = false) @Parameter(description = "Фильтр по мин цене товара", required = false) BigDecimal minPrice,
            @RequestParam(name = "max_price", required = false) @Parameter(description = "Фильтр по макс цене товара", required = false) BigDecimal maxPrice,
            @RequestParam(name = "title", required = false) @Parameter(description = "Фильтр части названия товара", required = false) String title,
            @RequestParam(name = "category", required = false) @Parameter(description = "Фильтр категории товара", required = false) String category
    ) {
        if (page < 1) {
            page = 1;
        }
        return productService.findAll(minPrice, maxPrice, title, category, page).map(productDtoMapper::map);
    }

    @Operation(
            summary = "Запрос на получение товара по id",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))
                    ),
                    @ApiResponse(
                            description = "Товар не найден", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable @Parameter(description = "Идентификатор товара", required = true) Long id) {
        return productService.findById(id).map(productDtoMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт с id: " + id + " не найден"));
    }

    @Operation(
            summary = "Запрос на создание нового товара",
            responses = {
                    @ApiResponse(
                            description = "Продукт успешно создан", responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Категория товара не найдена", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewProducts(@RequestBody ProductDto productDto) {
        productService.createNewProduct(productDtoMapper.map(productDto, categoryService));
    }

    @Operation(
            summary = "Запрос на удаление товара по id",
            responses = {
                    @ApiResponse(
                            description = "Товар удалён", responseCode = "200"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable @Parameter(description = "Идентификатор товара", required = true) Long id) {
        productService.deleteById(id);
    }
}
