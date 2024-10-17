package ru.mediasoft.shop.kafka.eventData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mediasoft.shop.controller.dto.order.OrderRequest;
import ru.mediasoft.shop.enumeration.Event;
import ru.mediasoft.shop.kafka.KafkaEvent;

import java.util.List;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderEventData implements KafkaEvent {

    private UUID orderId;
    private Long customerId;
    private List<OrderRequest.CompressedProduct> products;

    @Override
    public Event getEvent() {
        return Event.UPDATE_ORDER;
    }
}
