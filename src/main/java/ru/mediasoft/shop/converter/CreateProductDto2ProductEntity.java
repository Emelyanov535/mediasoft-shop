package ru.mediasoft.shop.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.mediasoft.shop.persistence.entity.ProductEntity;
import ru.mediasoft.shop.service.dto.CreateProductDto;

import java.time.LocalDateTime;
import java.util.UUID;
@Component
public class CreateProductDto2ProductEntity implements Converter<CreateProductDto, ProductEntity> {
    @Override
    public ProductEntity convert(CreateProductDto source) {
        return ProductEntity.builder()
                .id(UUID.randomUUID())
                .name(source.getName())
                .article(source.getArticle())
                .description(source.getDescription())
                .category(source.getCategory())
                .price(source.getPrice())
                .amount(source.getAmount())
                .changedAmount(LocalDateTime.now())
                .createdAt(LocalDateTime.now().toLocalDate())
                .build();
    }
}
