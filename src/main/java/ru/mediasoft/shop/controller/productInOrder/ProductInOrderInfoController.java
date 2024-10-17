package ru.mediasoft.shop.controller.productInOrder;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mediasoft.shop.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/info")
@Tag(name = "Сложный запрос", description = "Получение информации о продуктах и в каких заказах они находятся")
public class ProductInOrderInfoController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Map<UUID, List<OrderInfo>>> getProductInOrderInfo(@RequestBody ProductIdsList productIdsList) {
        return ResponseEntity.ok(orderService.getProductInOrderInfo(productIdsList.getProductIds()));
    }
}
