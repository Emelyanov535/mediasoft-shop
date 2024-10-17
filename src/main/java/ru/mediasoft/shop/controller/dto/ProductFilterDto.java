package ru.mediasoft.shop.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.mediasoft.shop.enumeration.CategoryType;

import java.math.BigDecimal;

@Builder
@Data
public class ProductFilterDto {
    private String name;
    private CategoryType category;
    private BigDecimal price;
    private Integer amount;
    @NotNull(message = "Page cannot be null")
    private int page;
    @NotNull(message = "Size cannot be null")
    private int size;
}
