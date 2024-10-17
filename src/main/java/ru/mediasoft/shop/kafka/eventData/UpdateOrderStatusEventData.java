package ru.mediasoft.shop.kafka.eventData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mediasoft.shop.enumeration.Event;
import ru.mediasoft.shop.enumeration.OrderStatus;
import ru.mediasoft.shop.kafka.KafkaEvent;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusEventData implements KafkaEvent {
    private Long customerId;
    private UUID orderId;
    private OrderStatus orderStatus;

    @Override
    public Event getEvent() {
        return Event.UPDATE_ORDER_STATUS;
    }
}
