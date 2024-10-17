package ru.mediasoft.shop.kafka.eventData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mediasoft.shop.enumeration.Event;
import ru.mediasoft.shop.kafka.KafkaEvent;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteOrderEventData implements KafkaEvent {

    private UUID orderId;
    private Long customerId;

    @Override
    public Event getEvent() {
        return Event.DELETE_ORDER;
    }
}
