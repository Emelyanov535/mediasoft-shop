package ru.mediasoft.shop.controller.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mediasoft.shop.enumeration.OrderStatus;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusRequest {
    private OrderStatus status;
}
