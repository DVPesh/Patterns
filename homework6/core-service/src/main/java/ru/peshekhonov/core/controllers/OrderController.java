package ru.peshekhonov.core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.peshekhonov.api.dto.OrderDetailsDto;
import ru.peshekhonov.api.dto.OrderDto;
import ru.peshekhonov.api.exceptions.AppError;
import ru.peshekhonov.core.services.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Заказы", description = "Методы работы с заказами")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Запрос на оформление заказа пользователя",
            responses = {
                    @ApiResponse(
                            description = "Заказ успешно создан", responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Продукт не найден", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestHeader @Parameter(description = "Логин пользователя", required = true) String username,
                            @RequestBody(required = false) Optional<OrderDetailsDto> orderDetailsDto) {
        orderService.createOrder(username, orderDetailsDto);
    }

    @Operation(
            summary = "Запрос на получение всех заказов пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = List.class))
                    )
            }
    )
    @GetMapping
    public List<OrderDto> getCurrentUserOrders(@RequestHeader
                                               @Parameter(description = "Логин пользователя", required = true) String username) {
        return orderService.findAllOrderDtoByUsername(username);
    }
}
