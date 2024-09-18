package ru.mediasoft.shop.service;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mediasoft.shop.exception.ProductArticleAlreadyExistsException;
import ru.mediasoft.shop.exception.ProductNotFoundException;
import ru.mediasoft.shop.persistence.entity.ProductEntity;
import ru.mediasoft.shop.persistence.repository.ProductRepository;
import ru.mediasoft.shop.service.dto.CreateProductDto;
import ru.mediasoft.shop.service.dto.ProductDto;
import ru.mediasoft.shop.service.dto.UpdateProductDto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ConversionService conversionService;

    @Transactional(readOnly = true)
    public ProductDto findProductById(UUID id){
        final ProductEntity productEntity = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return conversionService.convert(productEntity, ProductDto.class);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(Pageable pageable) {
        return productRepository
                .findAll(pageable)
                .map(product -> conversionService.convert(product, ProductDto.class));
    }

    @Transactional
    public UUID createProduct(CreateProductDto createProductDto){
        if (productRepository.existsByArticle(createProductDto.getArticle())){
            throw new ProductArticleAlreadyExistsException(createProductDto.getArticle());
        }
        final ProductEntity productEntity = conversionService.convert(createProductDto, ProductEntity.class);

        return productRepository.save(productEntity).getId();
    }

    @Transactional
    public UUID updateProduct(UUID id, UpdateProductDto updateProductDto){
        final ProductDto productDto = findProductById(id);
        final ProductEntity existingProductEntity = conversionService.convert(productDto, ProductEntity.class);
        if (updateProductDto.getArticle() != null && !updateProductDto.getArticle().equals(existingProductEntity.getArticle())) {
            if (productRepository.existsByArticle(updateProductDto.getArticle())) {
                throw new ProductArticleAlreadyExistsException(updateProductDto.getArticle());
            }
            existingProductEntity.setArticle(updateProductDto.getArticle());
        }
        if (updateProductDto.getName() != null && !Objects.equals(existingProductEntity.getName(), updateProductDto.getName())) {
            existingProductEntity.setName(updateProductDto.getName());
        }
        if (updateProductDto.getDescription() != null && !Objects.equals(existingProductEntity.getDescription(), updateProductDto.getDescription())) {
            existingProductEntity.setDescription(updateProductDto.getDescription());
        }
        if (updateProductDto.getCategory() != null && !Objects.equals(existingProductEntity.getCategory(), updateProductDto.getCategory())) {
            existingProductEntity.setCategory(updateProductDto.getCategory());
        }
        if (updateProductDto.getPrice() != null && !Objects.equals(existingProductEntity.getPrice(), updateProductDto.getPrice())) {
            existingProductEntity.setPrice(updateProductDto.getPrice());
        }
        if (updateProductDto.getAmount() != null && !Objects.equals(existingProductEntity.getAmount(), updateProductDto.getAmount())) {
            if(updateProductDto.getAmount() >= 0){
                existingProductEntity.setAmount(updateProductDto.getAmount());
                existingProductEntity.setChangedAmount(LocalDateTime.now());
            }
        }

        return productRepository.save(existingProductEntity).getId();
    }
    @Transactional
    public void deleteProduct(UUID id){
        final ProductEntity productEntity = conversionService.convert(findProductById(id), ProductEntity.class);
        productRepository.delete(productEntity);
    }
}
