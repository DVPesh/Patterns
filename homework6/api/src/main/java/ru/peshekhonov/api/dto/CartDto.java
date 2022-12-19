package ru.peshekhonov.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class CartDto {

    private List<CartItemDto> items;
    private BigDecimal totalCost;
}
