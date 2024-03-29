package ru.peshekhonov.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.peshekhonov.api.dto.CartDto;
import ru.peshekhonov.api.dto.OrderDetailsDto;
import ru.peshekhonov.api.dto.OrderDto;
import ru.peshekhonov.api.exceptions.ResourceNotFoundException;
import ru.peshekhonov.core.entities.Order;
import ru.peshekhonov.core.entities.OrderItem;
import ru.peshekhonov.core.exceptions.OrderNotCreatedException;
import ru.peshekhonov.core.integrations.CartServiceIntegration;
import ru.peshekhonov.core.mappers.OrderDtoMapper;
import ru.peshekhonov.core.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartServiceIntegration cartServiceIntegration;
    private final ProductService productService;
    private final OrderDtoMapper orderDtoMapper;

    @Transactional
    public void createOrder(String username, Optional<OrderDetailsDto> orderDetailsDto) {
        CartDto cart = cartServiceIntegration.getCurrentCart(username);
        if (cart.getItems().size() == 0) {
            throw new OrderNotCreatedException("Корзина пуста, заказ не может быть создан");
        }
        Order order = new Order();
        if (orderDetailsDto.isPresent()) {
            order.setAddress(orderDetailsDto.get().getAddress());
            order.setPhone(orderDetailsDto.get().getPhone());
        }
        order.setUsername(username);
        order.setTotalCost(cart.getTotalCost());
        List<OrderItem> items = cart.getItems().stream()
                .map(o -> {
                    Long productId = o.getProductId();
                    return OrderItem.builder()
                            .order(order)
                            .quantity(o.getQuantity())
                            .pricePerProduct(o.getPricePerProduct())
                            .cost(o.getCost())
                            .product(productService.findById(productId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Продукт с id: " + productId + " не найден")))
                            .build();
                }).collect(Collectors.toList());
        order.setItems(items);
        orderRepository.save(order);
        cartServiceIntegration.clear(username);
    }

    public List<Order> findOrdersByUsername(String username) {
        return orderRepository.findByUsername(username);
    }

    public List<OrderDto> findAllOrderDtoByUsername(String username) {
        return orderDtoMapper.map(orderRepository.findByUsername(username));
    }
}
