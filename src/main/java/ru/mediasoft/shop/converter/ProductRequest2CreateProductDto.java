package ru.mediasoft.shop.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.mediasoft.shop.controller.dto.ProductRequest;
import ru.mediasoft.shop.service.dto.CreateProductDto;

@Component
public class ProductRequest2CreateProductDto implements Converter<ProductRequest, CreateProductDto> {
    @Override
    public CreateProductDto convert(ProductRequest source) {
        return CreateProductDto.builder()
                .name(source.getName())
                .article(source.getArticle())
                .description(source.getDescription())
                .category(source.getCategory())
                .price(source.getPrice())
                .amount(source.getAmount())
                .build();
    }
}
