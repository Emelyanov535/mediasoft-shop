package ru.mediasoft.shop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.mediasoft.shop.persistence.entity.CategoryType;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProductDto {
    private String name;
    private Long article;
    private String description;
    private CategoryType category;
    @Nullable
    private BigDecimal price;
    private int amount;
}
