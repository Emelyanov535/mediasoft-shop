package ru.mediasoft.shop.controller.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {
    private String deliveryAddress;
    private List<CompressedProduct> products;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class CompressedProduct{
        private UUID id;
        private Integer quantity;
    }
}
