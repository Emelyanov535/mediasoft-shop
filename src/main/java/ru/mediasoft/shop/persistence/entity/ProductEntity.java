package ru.mediasoft.shop.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.mediasoft.shop.enumeration.CategoryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID")
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private Long article;
    private String description;
    @Enumerated(EnumType.STRING)
    private CategoryType category;
    @Column(columnDefinition = "DECIMAL(10, 2)", nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private int amount;
    private LocalDateTime changedAmount;
    private LocalDate createdAt;
}
