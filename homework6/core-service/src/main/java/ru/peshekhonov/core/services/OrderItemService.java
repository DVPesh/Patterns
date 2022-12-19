package ru.peshekhonov.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.peshekhonov.core.entities.OrderItem;
import ru.peshekhonov.core.repositories.OrderItemRepository;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItem save(OrderItem item) {
        return orderItemRepository.save(item);
    }
}
