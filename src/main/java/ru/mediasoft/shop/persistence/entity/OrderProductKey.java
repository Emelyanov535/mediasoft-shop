package ru.mediasoft.shop.persistence.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class OrderProductKey implements Serializable {
    @JoinColumn(name = "order_id")
    private UUID order;
    @JoinColumn(name = "product_id")
    private UUID product;
}
