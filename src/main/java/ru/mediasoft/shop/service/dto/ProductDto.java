package ru.mediasoft.shop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.mediasoft.shop.enumeration.CategoryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class ProductDto {
    private UUID id;
    private String name;
    private Long article;
    private String description;
    private CategoryType category;
    private BigDecimal price;
    private int amount;
    private LocalDateTime changedAmount;
    private LocalDate createdAt;
    private String currency;
    private Boolean isAvailable;
}
