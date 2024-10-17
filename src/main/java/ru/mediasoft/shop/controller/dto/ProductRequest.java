package ru.mediasoft.shop.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProductRequest {
    @NotBlank(message = "Product name cannot be blank")
    private String name;
    @NotNull(message = "Product article cannot be null")
    @Min(value = 1, message = "Product article must be over 1")
    private Long article;
    @NotBlank(message = "Product description cannot be blank")
    private String description;
    @NotNull(message = "Product category cannot be null")
    private CategoryType category;
    @NotNull(message = "Product price cannot be null")
    @Min(value = 1, message = "Product price must be over 1")
    private BigDecimal price;
    @Min(value = 0, message = "Product amount must be over 0")
    private int amount;
}
