package ru.mediasoft.shop.kafka;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.mediasoft.shop.controller.dto.EventSource;
import ru.mediasoft.shop.kafka.eventData.CreateOrderEventData;
import ru.mediasoft.shop.kafka.eventData.DeleteOrderEventData;
import ru.mediasoft.shop.kafka.eventData.UpdateOrderEventData;
import ru.mediasoft.shop.kafka.eventData.UpdateOrderStatusEventData;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "event"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateOrderEventData.class, name = "CREATE_ORDER"),
        @JsonSubTypes.Type(value = UpdateOrderEventData.class, name = "UPDATE_ORDER"),
        @JsonSubTypes.Type(value = DeleteOrderEventData.class, name = "DELETE_ORDER"),
        @JsonSubTypes.Type(value = UpdateOrderStatusEventData.class, name = "UPDATE_ORDER_STATUS"),
})
public interface KafkaEvent extends EventSource {
}
