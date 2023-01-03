package ru.peshekhonov.core.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.peshekhonov.core.events.ProductCreatedEvent;
import ru.peshekhonov.core.events.ProductDeletedEvent;
import ru.peshekhonov.core.mappers.ProductDtoMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoreServiceEventListener {

    private final ProductDtoMapper productDtoMapper;

    @Async
    @EventListener
    public void handleEvent(ProductCreatedEvent event) {
        log.info("Добавлен новый товар: {}", productDtoMapper.map(event.getProduct()));
    }

    @Async
    @EventListener
    public void handleEvent(ProductDeletedEvent event) {
        log.info("Удален товар: {}", productDtoMapper.map(event.getProduct()));
    }
}
