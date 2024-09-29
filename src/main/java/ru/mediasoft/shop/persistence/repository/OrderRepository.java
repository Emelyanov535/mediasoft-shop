package ru.mediasoft.shop.persistence.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mediasoft.shop.persistence.entity.OrderEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @EntityGraph(attributePaths = {"customer", "orderProducts.product"})
    Optional<OrderEntity> findById(@Param("id") UUID id);
}