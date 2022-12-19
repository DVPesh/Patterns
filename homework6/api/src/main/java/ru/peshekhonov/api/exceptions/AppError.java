package ru.peshekhonov.api.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Сообщение об ошибке")
public class AppError {

    @Schema(description = "Тип ошибки", requiredMode = Schema.RequiredMode.REQUIRED, example = "RESOURCE_NOT_FOUND")
    private String code;

    @Schema(description = "Описание ошибки", requiredMode = Schema.RequiredMode.REQUIRED, example = "Продукт с id: 2 не найден")
    private String error;
}
