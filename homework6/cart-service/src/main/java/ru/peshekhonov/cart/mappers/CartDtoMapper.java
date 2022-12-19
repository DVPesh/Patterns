package ru.peshekhonov.cart.mappers;

import org.mapstruct.Mapper;
import ru.peshekhonov.api.dto.CartDto;
import ru.peshekhonov.cart.utils.Cart;

@Mapper(componentModel = "spring", uses = {CartItemDtoMapper.class})
public interface CartDtoMapper {

    CartDto map(Cart cart);
}
