package ru.mediasoft.shop.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mediasoft.shop.persistence.entity.OrderProductEntity;
import ru.mediasoft.shop.persistence.entity.OrderProductKey;
import ru.mediasoft.shop.service.dto.CompressedProductForOrderProjection;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProductEntity, OrderProductKey> {

    @Query("""
            SELECT new ru.mediasoft.shop.service.dto.CompressedProductForOrderProjection(
            p.id,
            p.name,
            op.quantity,
            op.price)
            FROM OrderProductEntity op
            JOIN op.product p
            WHERE op.order.id = :orderId""")
    List<CompressedProductForOrderProjection> findProductsByOrderId(UUID orderId);
}
