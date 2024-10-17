package ru.mediasoft.shop.controller.productInOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mediasoft.shop.enumeration.OrderStatus;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderInfo {
    private UUID id;
    private CustomerInfo customer;
    private OrderStatus status;
    private String deliveryAddress;
    private Integer quantity;
}
