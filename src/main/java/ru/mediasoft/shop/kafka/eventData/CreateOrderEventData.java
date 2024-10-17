package ru.mediasoft.shop.kafka.eventData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mediasoft.shop.controller.dto.order.OrderRequest;
import ru.mediasoft.shop.enumeration.Event;
import ru.mediasoft.shop.kafka.KafkaEvent;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderEventData implements KafkaEvent {
    private Long customerId;
    private String deliveryAddress;
    private List<OrderRequest.CompressedProduct> products;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Event getEvent() {
        return Event.CREATE_ORDER;
    }
}
