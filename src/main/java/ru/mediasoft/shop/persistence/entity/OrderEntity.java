package ru.mediasoft.shop.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.mediasoft.shop.enumeration.OrderStatus;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "t_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID")
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @Column(nullable = false)
    private String deliveryAddress;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "customer")
    private CustomerEntity customer;
    @OneToMany(mappedBy = "key.order",
            cascade = CascadeType.ALL)
    private List<OrderProductEntity> orderProducts;
}




