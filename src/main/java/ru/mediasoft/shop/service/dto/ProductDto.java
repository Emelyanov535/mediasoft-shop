package ru.mediasoft.shop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import ru.mediasoft.shop.persistence.entity.CategoryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Value
public class ProductDto {
    UUID id;
    String name;
    Long article;
    String description;
    CategoryType category;
    BigDecimal price;
    int amount;
    LocalDateTime changedAmount;
    LocalDate createdAt;
}
