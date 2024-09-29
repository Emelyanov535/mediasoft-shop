package ru.mediasoft.shop.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mediasoft.shop.configuration.filter.CustomerIdRequestBean;
import ru.mediasoft.shop.controller.dto.order.OrderRequest;
import ru.mediasoft.shop.controller.dto.order.OrderResponse;
import ru.mediasoft.shop.controller.dto.order.StatusRequest;
import ru.mediasoft.shop.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/order")
@Tag(name = "Заказ", description = "Взаимодействие с заказами")
public class OrderController {
    private final OrderService orderService;
    private final CustomerIdRequestBean customerIdRequestBean;

    @PostMapping
    public ResponseEntity<UUID> createOrder(
            @RequestBody OrderRequest orderRequest) {

        return ResponseEntity.ok(orderService.createOrder(customerIdRequestBean.getCustomerId(), orderRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(orderService.findOrderById(customerIdRequestBean.getCustomerId(), id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UUID> addProductToOrder(
            @PathVariable UUID id,
            @RequestBody List<OrderRequest.CompressedProduct> products) {

        return ResponseEntity.ok(orderService.addProductToOrder(customerIdRequestBean.getCustomerId(), id, products));
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(
            @PathVariable UUID id) {

        orderService.deleteOrder(id, customerIdRequestBean.getCustomerId());
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(
            @PathVariable String id) {

        //TODO: сервисный метод не делает ничего
        return ResponseEntity.ok(orderService.confirmOrder(customerIdRequestBean.getCustomerId()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UUID> changeOrderStatus(
            @PathVariable UUID id,
            @RequestBody StatusRequest statusRequest) {
        return ResponseEntity.ok(orderService.changeOrderStatus(customerIdRequestBean.getCustomerId(), id, statusRequest.getStatus()));
    }
}
