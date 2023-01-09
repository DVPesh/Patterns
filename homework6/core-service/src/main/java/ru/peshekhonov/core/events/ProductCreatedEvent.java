package ru.peshekhonov.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.peshekhonov.core.entities.Product;

@Getter
@RequiredArgsConstructor
public class ProductCreatedEvent {

    private final Product product;
}
