package ru.mediasoft.shop.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.mediasoft.shop.controller.dto.UpdateProductRequest;
import ru.mediasoft.shop.service.dto.UpdateProductDto;
@Component
public class UpdateProductRequest2UpdateProductDto implements Converter<UpdateProductRequest, UpdateProductDto> {
    @Override
    public UpdateProductDto convert(UpdateProductRequest source) {
        return UpdateProductDto.builder()
                .name(source.getName())
                .article(source.getArticle())
                .description(source.getDescription())
                .category(source.getCategory())
                .price(source.getPrice())
                .amount(source.getAmount())
                .build();
    }
}
