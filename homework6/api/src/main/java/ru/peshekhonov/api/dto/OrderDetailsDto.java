package ru.peshekhonov.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Информация о заказе")
public class OrderDetailsDto {

    @Schema(description = "Адрес", requiredMode = Schema.RequiredMode.AUTO, example = "г. Омск ул. Вавилова дом 5 кв. 4")
    private String address;

    @Schema(description = "Телефон", requiredMode = Schema.RequiredMode.AUTO, example = "+74993452311")
    private String phone;
}
