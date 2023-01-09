package ru.peshekhonov.core.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.peshekhonov.api.dto.ProductDto;
import ru.peshekhonov.core.events.ProductCreatedEvent;
import ru.peshekhonov.core.events.ProductDeletedEvent;
import ru.peshekhonov.core.mappers.ProductDtoMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoreServiceEventListener {

    private final ProductDtoMapper productDtoMapper;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    @EventListener
    public void handleEvent(ProductCreatedEvent event) {
        ProductDto productDto = productDtoMapper.map(event.getProduct());
        log.info("Добавлен новый товар: {}", productDto);
        rabbitTemplate.convertAndSend("STOMP", null,
                String.format("В категории '%s' добавлен новый товар '%s' id=%s, цена %s", productDto.getCategoryTitle(),
                        productDto.getTitle(), productDto.getId(), productDto.getPrice()));
    }

    @Async
    @EventListener
    public void handleEvent(ProductDeletedEvent event) {
        ProductDto productDto = productDtoMapper.map(event.getProduct());
        log.info("Удален товар: {}", productDto);
        rabbitTemplate.convertAndSend("STOMP", null,
                String.format("Из категории '%s' удалён товар '%s' id=%s, цена %s", productDto.getCategoryTitle(),
                        productDto.getTitle(), productDto.getId(), productDto.getPrice()));
    }
}
