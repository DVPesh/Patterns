package ru.peshekhonov.core.mappers;

import org.mapstruct.Mapper;
import ru.peshekhonov.api.dto.OrderDto;
import ru.peshekhonov.core.entities.Order;

import java.util.List;

@Mapper(componentModel = "spring", uses = OrderItemDtoMapper.class)
public interface OrderDtoMapper {

    OrderDto map(Order order);

    List<OrderDto> map(List<Order> orders);
}
