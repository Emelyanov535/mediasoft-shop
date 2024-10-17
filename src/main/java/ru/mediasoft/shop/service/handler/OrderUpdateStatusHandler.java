package ru.mediasoft.shop.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mediasoft.shop.controller.dto.EventSource;
import ru.mediasoft.shop.enumeration.Event;
import ru.mediasoft.shop.kafka.eventData.UpdateOrderStatusEventData;
import ru.mediasoft.shop.service.OrderService;

@Component
@RequiredArgsConstructor
public class OrderUpdateStatusHandler implements EventHandler<UpdateOrderStatusEventData> {
    private final OrderService orderService;

    @Override
    public boolean canHandle(EventSource eventSource) {
        return Event.UPDATE_ORDER_STATUS.equals(eventSource.getEvent());
    }

    @Override
    public void handleEvent(UpdateOrderStatusEventData eventSource) {
        orderService.changeOrderStatus(eventSource.getCustomerId(), eventSource.getOrderId(), eventSource.getOrderStatus());
    }
}
