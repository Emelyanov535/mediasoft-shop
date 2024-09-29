package ru.mediasoft.shop.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mediasoft.shop.controller.dto.order.OrderRequest;
import ru.mediasoft.shop.controller.dto.order.OrderResponse;
import ru.mediasoft.shop.enumeration.OrderStatus;
import ru.mediasoft.shop.exception.NotAvailableOrNotEnoughAmountException;
import ru.mediasoft.shop.exception.NotExpectedCustomerException;
import ru.mediasoft.shop.exception.OrderNotFoundException;
import ru.mediasoft.shop.exception.ProductNotFoundException;
import ru.mediasoft.shop.persistence.entity.*;
import ru.mediasoft.shop.persistence.repository.OrderProductRepository;
import ru.mediasoft.shop.persistence.repository.OrderRepository;
import ru.mediasoft.shop.persistence.repository.ProductRepository;
import ru.mediasoft.shop.service.dto.CompressedProductForOrderProjection;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final CustomerService customerService;
    private final ProductRepository productRepository;

    @Transactional
    public UUID createOrder(Long customerId, OrderRequest orderRequest) {
        final CustomerEntity customer = customerService.findCustomerById(customerId);

        final OrderEntity order = OrderEntity.builder()
                .status(OrderStatus.CREATED)
                .deliveryAddress(orderRequest.getDeliveryAddress())
                .customer(customer)
                .build();

        orderRepository.save(order);

        Map<UUID, ProductEntity> listProduct = productRepository.findAllById(
                        orderRequest.getProducts().stream()
                                .map(OrderRequest.CompressedProduct::getId)
                                .toList())
                .stream()
                .collect(Collectors.toMap(ProductEntity::getId, productEntity -> productEntity));


        for (OrderRequest.CompressedProduct product : orderRequest.getProducts()) {
            ProductEntity productEntity = listProduct.get(product.getId());

            if (isAvailableAndEnoughAmount(productEntity, product.getQuantity())) {
                OrderProductEntity orderProductEntity = OrderProductEntity.builder()
                        .key(new OrderProductKey(order, productEntity))
                        .quantity(product.getQuantity())
                        .price(productEntity.getPrice())
                        .build();

                int productAmount = productEntity.getAmount();
                productEntity.setAmount(productAmount - product.getQuantity());

                orderProductRepository.save(orderProductEntity);
                productRepository.save(productEntity);
            } else {
                throw new NotAvailableOrNotEnoughAmountException(productEntity.getId());
            }
        }

        return order.getId();
    }

    public OrderResponse findOrderById(Long customerId, UUID id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (checkCustomer(order, customerId)) {
            throw new NotExpectedCustomerException();
        }

        List<CompressedProductForOrderProjection> products = orderProductRepository.findProductsByOrderId(id);

        BigDecimal totalPrice = products.stream()
                .map(product -> BigDecimal.valueOf(product.getQuantity()).multiply(product.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderResponse.builder()
                .orderId(order.getId())
                .products(products)
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional
    public void deleteOrder(UUID orderId, Long customerId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (checkCustomer(order, customerId)) {
            throw new NotExpectedCustomerException();
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Order status is not CREATED");
        }

        order.setStatus(OrderStatus.CANCELLED);

        for (OrderProductEntity productInOrder : order.getOrderProducts()) {
            ProductEntity productEntity = productInOrder.getProduct();
            productEntity.setAmount(productEntity.getAmount() + productInOrder.getQuantity());
            orderProductRepository.delete(productInOrder);
        }

        order.getOrderProducts().clear();
    }

    @Transactional
    public UUID addProductToOrder(Long customerId, UUID orderId, List<OrderRequest.CompressedProduct> products) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (checkCustomer(order, customerId)) {
            throw new NotExpectedCustomerException();
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Order status is not CREATED");
        }

        Map<UUID, ProductEntity> productMap = productRepository.findAllById(
                        products.stream()
                                .map(OrderRequest.CompressedProduct::getId)
                                .toList())
                .stream()
                .collect(Collectors.toMap(ProductEntity::getId, productEntity -> productEntity));


        for (OrderRequest.CompressedProduct newProduct : products) {
            ProductEntity productEntity = productMap.get(newProduct.getId());
            if (productEntity == null) {
                throw new ProductNotFoundException(newProduct.getId());
            }

            if (!isAvailableAndEnoughAmount(productEntity, newProduct.getQuantity())) {
                throw new NotAvailableOrNotEnoughAmountException(productEntity.getId());
            }

            // Проверяем, есть ли продукт в заказе
            Optional<OrderProductEntity> existingOrderProduct = order.getOrderProducts().stream()
                    .filter(orderProduct -> orderProduct.getProduct().getId().equals(newProduct.getId()))
                    .findFirst();

            if (existingOrderProduct.isPresent()) {
                // Продукт уже есть в заказе
                OrderProductEntity productInOrder = existingOrderProduct.get();
                productInOrder.setQuantity(productInOrder.getQuantity() + newProduct.getQuantity()); // увеличиваем количество в заказе
                productEntity.setAmount(productEntity.getAmount() - newProduct.getQuantity()); // уменьшаем количество на складе
                productInOrder.setPrice(productEntity.getPrice()); // обновляем цену
            } else {
                // Продукт не найден в заказе, добавляем новый
                OrderProductEntity orderProductEntity = OrderProductEntity.builder()
                        .key(new OrderProductKey(order, productEntity))
                        .quantity(newProduct.getQuantity())
                        .price(productEntity.getPrice())
                        .build();

                order.getOrderProducts().add(orderProductEntity);
            }
        }

        return orderRepository.save(order).getId();
    }

    public OrderResponse confirmOrder(Long customerId) {
        //TODO сервисный метод не делает ничего
        return null;
    }

    public UUID changeOrderStatus(Long customerId, UUID orderId, OrderStatus status) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (checkCustomer(order, customerId)) {
            throw new NotExpectedCustomerException();
        }

        order.setStatus(status);

        return orderRepository.save(order).getId();
    }

    private boolean checkCustomer(OrderEntity order, Long customerId) {
        CustomerEntity customer = customerService.findCustomerById(customerId);
        return !Objects.equals(customer.getId(), order.getCustomer().getId());
    }

    private boolean isAvailableAndEnoughAmount(ProductEntity product, Integer quantity) {
        return product.getIsAvailable() && product.getAmount() >= quantity && quantity > 0;
    }
}
