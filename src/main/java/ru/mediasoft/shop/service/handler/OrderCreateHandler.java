package ru.mediasoft.shop.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mediasoft.shop.controller.dto.EventSource;
import ru.mediasoft.shop.controller.dto.order.OrderRequest;
import ru.mediasoft.shop.enumeration.Event;
import ru.mediasoft.shop.kafka.eventData.CreateOrderEventData;
import ru.mediasoft.shop.service.OrderService;

@Component
@RequiredArgsConstructor
public class OrderCreateHandler implements EventHandler<CreateOrderEventData> {

    private final OrderService orderService;

    @Override
    public boolean canHandle(EventSource eventSource) {
        return Event.CREATE_ORDER.equals(eventSource.getEvent());
    }

    @Override
    public void handleEvent(CreateOrderEventData eventSource) {
        orderService.createOrder(eventSource.getCustomerId(), new OrderRequest(eventSource.getDeliveryAddress(), eventSource.getProducts()));
    }
}
