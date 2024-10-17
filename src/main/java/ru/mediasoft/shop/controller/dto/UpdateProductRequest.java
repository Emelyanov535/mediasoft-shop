package ru.mediasoft.shop.controller.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mediasoft.shop.enumeration.CategoryType;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateProductRequest {
    private String name;
    @Min(value = 1, message = "Product article must be over 1")
    private Long article;
    private String description;
    private CategoryType category;
    @Min(value = 1, message = "Product price must be over 1")
    private BigDecimal price;
    private Integer amount;
}
