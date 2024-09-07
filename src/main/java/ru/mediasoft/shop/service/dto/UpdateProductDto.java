package ru.mediasoft.shop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mediasoft.shop.persistence.entity.CategoryType;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateProductDto {
    private String name;
    private Long article;
    private String description;
    private CategoryType category;
    private BigDecimal price;
    private Integer amount;
}
