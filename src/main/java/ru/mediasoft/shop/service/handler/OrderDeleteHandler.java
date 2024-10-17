package ru.mediasoft.shop.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mediasoft.shop.controller.dto.EventSource;
import ru.mediasoft.shop.enumeration.Event;
import ru.mediasoft.shop.kafka.eventData.DeleteOrderEventData;
import ru.mediasoft.shop.service.OrderService;

@Component
@RequiredArgsConstructor
public class OrderDeleteHandler implements EventHandler<DeleteOrderEventData> {

    private final OrderService orderService;

    @Override
    public boolean canHandle(EventSource eventSource) {
        return Event.DELETE_ORDER.equals(eventSource.getEvent());
    }

    @Override
    public void handleEvent(DeleteOrderEventData eventSource) {
        orderService.deleteOrder(eventSource.getOrderId(), eventSource.getCustomerId());
    }
}
