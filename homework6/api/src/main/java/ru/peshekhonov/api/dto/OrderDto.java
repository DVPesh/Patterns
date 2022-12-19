package ru.peshekhonov.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDto {

    private Long id;
    private String username;
    private List<OrderItemDto> items;
    private BigDecimal totalCost;
    private String address;
    private String phone;
}
