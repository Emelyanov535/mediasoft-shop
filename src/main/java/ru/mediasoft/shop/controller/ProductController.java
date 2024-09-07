package ru.mediasoft.shop.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mediasoft.shop.controller.dto.ProductRequest;
import ru.mediasoft.shop.controller.dto.UpdateProductRequest;
import ru.mediasoft.shop.service.ProductService;
import ru.mediasoft.shop.service.dto.CreateProductDto;
import ru.mediasoft.shop.service.dto.ProductDto;
import ru.mediasoft.shop.service.dto.UpdateProductDto;

import java.util.Objects;
import java.util.UUID;

/**
 * REST-контроллер для управления продуктами.
 * Обрабатывает запросы для создания, обновления, удаления и получения продуктов.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/product")
@Tag(name = "Продукты", description = "Взаимодействие с продуктами")
public class ProductController {
    private final ProductService productService;
    private final ConversionService conversionService;

    /**
     * Получение продукта по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор продукта.
     * @return {@link ResponseEntity} с продуктом {@link ProductDto}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable UUID id){
        return ResponseEntity.ok(productService.findProductById(id));
    }

    /**
     * Получение списка всех продуктов с возможностью пагинации.
     *
     * @param pageable Объект {@link Pageable} для управления параметрами пагинации.
     * @return Страница продуктов {@link Page} с объектами {@link ProductDto}.
     */
    @GetMapping()
    public Page<ProductDto> findAll(Pageable pageable){
        return productService.findAll(pageable);
    }

    /**
     * Создание нового продукта.
     *
     * @param productRequest Запрос на создание продукта {@link ProductRequest}.
     * @return {@link ResponseEntity} с уникальным идентификатором созданного продукта {@link UUID}.
     */
    @PostMapping
    public ResponseEntity<UUID> createProduct(@RequestBody @Validated ProductRequest productRequest){
        return ResponseEntity.ok(productService.createProduct(Objects.requireNonNull(conversionService.convert(productRequest, CreateProductDto.class))));
    }

    /**
     * Обновление информации о продукте.
     *
     * @param id Уникальный идентификатор продукта.
     * @param updateProductRequest Запрос на обновление продукта {@link UpdateProductRequest}.
     * @return {@link ResponseEntity} с уникальным идентификатором обновленного продукта {@link UUID}.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UUID> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Validated UpdateProductRequest updateProductRequest
    ){
        return ResponseEntity.ok(productService.updateProduct(id, Objects.requireNonNull(conversionService.convert(updateProductRequest, UpdateProductDto.class))));
    }

    /**
     * Удаление продукта по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор продукта.
     */
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable UUID id){
        productService.deleteProduct(id);
    }
}
