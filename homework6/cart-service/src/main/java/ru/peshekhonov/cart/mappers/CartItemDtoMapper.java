package ru.peshekhonov.cart.mappers;

import org.mapstruct.Mapper;
import ru.peshekhonov.api.dto.CartItemDto;
import ru.peshekhonov.cart.utils.CartItem;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemDtoMapper {

    CartItemDto map(CartItem cartItem);

    List<CartItemDto> map(List<CartItem> cartItems);
}
