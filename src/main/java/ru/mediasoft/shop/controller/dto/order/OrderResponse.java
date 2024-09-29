package ru.mediasoft.shop.controller.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mediasoft.shop.service.dto.CompressedProductForOrderProjection;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponse {
    private UUID orderId;
    private List<CompressedProductForOrderProjection> products;
    private BigDecimal totalPrice;
}
