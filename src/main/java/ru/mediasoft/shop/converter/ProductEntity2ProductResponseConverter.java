package ru.mediasoft.shop.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.mediasoft.shop.persistence.entity.ProductEntity;
import ru.mediasoft.shop.service.dto.ProductDto;
@Component
public class ProductEntity2ProductResponseConverter implements Converter<ProductEntity, ProductDto> {
    @Override
    public ProductDto convert(ProductEntity source) {
        return ProductDto.builder()
                .id(source.getId())
                .name(source.getName())
                .article(source.getArticle())
                .description(source.getDescription())
                .category(source.getCategory())
                .price(source.getPrice())
                .amount(source.getAmount())
                .changedAmount(source.getChangedAmount())
                .createdAt(source.getCreatedAt())
                .isAvailable(source.getIsAvailable())
                .build();
    }
}
