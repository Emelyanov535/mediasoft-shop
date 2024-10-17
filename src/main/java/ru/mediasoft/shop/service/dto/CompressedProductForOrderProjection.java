package ru.mediasoft.shop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompressedProductForOrderProjection {
    private UUID productId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
