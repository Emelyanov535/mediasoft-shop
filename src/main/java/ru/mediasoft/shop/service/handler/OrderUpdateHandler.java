package ru.mediasoft.shop.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mediasoft.shop.controller.dto.EventSource;
import ru.mediasoft.shop.enumeration.Event;
import ru.mediasoft.shop.kafka.eventData.UpdateOrderEventData;
import ru.mediasoft.shop.service.OrderService;

@Component
@RequiredArgsConstructor
public class OrderUpdateHandler implements EventHandler<UpdateOrderEventData> {

    private final OrderService orderService;

    @Override
    public boolean canHandle(EventSource eventSource) {
        return Event.UPDATE_ORDER.equals(eventSource.getEvent());
    }

    @Override
    public void handleEvent(UpdateOrderEventData eventSource) {
        orderService.addProductToOrder(eventSource.getCustomerId(), eventSource.getOrderId(), eventSource.getProducts());
    }
}
