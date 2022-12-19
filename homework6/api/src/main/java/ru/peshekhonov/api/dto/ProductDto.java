package ru.peshekhonov.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Schema(description = "Модель товара")
public class ProductDto {

    @Schema(description = "ID товара", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long id;

    @Schema(description = "Название товара", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 255,
            minLength = 3, example = "смартфон")
    private String title;

    @Schema(description = "Цена товара", requiredMode = Schema.RequiredMode.REQUIRED, example = "25200.34")
    private BigDecimal price;

    @Schema(description = "Категория товара", requiredMode = Schema.RequiredMode.REQUIRED, example = "техника и электроника")
    private String categoryTitle;
}
