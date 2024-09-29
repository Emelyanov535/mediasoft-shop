package ru.mediasoft.shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import ru.mediasoft.shop.enumeration.CategoryType;
import ru.mediasoft.shop.exception.ProductArticleAlreadyExistsException;
import ru.mediasoft.shop.exception.ProductNotFoundException;
import ru.mediasoft.shop.persistence.entity.ProductEntity;
import ru.mediasoft.shop.persistence.repository.ProductRepository;
import ru.mediasoft.shop.service.dto.CreateProductDto;
import ru.mediasoft.shop.service.dto.ProductDto;
import ru.mediasoft.shop.service.dto.UpdateProductDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepositoryMock;
    @Mock
    private ConversionService conversionServiceMock;
    @InjectMocks
    private ProductService underTest;
    @Test
    void findProductById_Success(){
        final UUID ID = UUID.randomUUID();
        final ProductEntity productEntity = ProductEntity.builder()
                .id(ID)
                .name("Test")
                .article(123L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(9990))
                .amount(15)
                .changedAmount(LocalDateTime.now())
                .createdAt(LocalDate.now())
                .build();

        when(productRepositoryMock.findById(ID)).thenReturn(Optional.ofNullable(productEntity));

        final ProductDto productDto = underTest.findProductById(ID);

        when(conversionServiceMock.convert(productDto, ProductEntity.class)).thenReturn(productEntity);

        final ProductEntity result = conversionServiceMock.convert(productDto, ProductEntity.class);

        assertNotNull(result);
        assertEquals(productEntity, result);
        verify(productRepositoryMock).findById(ID);
    }

    @Test
    void findProductById_UnSuccess(){
        var expected = UUID.randomUUID();
        when(productRepositoryMock.findById(any())).thenReturn(Optional.empty());

        ProductNotFoundException result = assertThrows(
                ProductNotFoundException.class,
                () -> underTest.findProductById(expected)
        );

        assertEquals("Product with id ["+expected+"] is not found", result.getMessage());
        verify(productRepositoryMock).findById(expected);
    }

    @Test
    void createProduct_Success(){
        final CreateProductDto createProductDto = CreateProductDto.builder()
                .name("Test")
                .article(123L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(9990))
                .amount(15)
                .build();
        final ProductEntity productEntity = ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .article(123L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(9990))
                .amount(15)
                .changedAmount(LocalDateTime.now())
                .createdAt(LocalDate.now())
                .build();

        when(productRepositoryMock.existsByArticle(createProductDto.getArticle())).thenReturn(false);
        when(conversionServiceMock.convert(createProductDto, ProductEntity.class)).thenReturn(productEntity);
        when(productRepositoryMock.save(productEntity)).thenReturn(productEntity);

        UUID result = underTest.createProduct(createProductDto);

        assertNotNull(result);
        assertEquals(productEntity.getId(), result);

        verify(productRepositoryMock).existsByArticle(createProductDto.getArticle());
        verify(conversionServiceMock).convert(createProductDto, ProductEntity.class);
        verify(productRepositoryMock).save(productEntity);
    }

    @Test
    void createProduct_UnSuccess(){
        final CreateProductDto createProductDto = CreateProductDto.builder()
                .name("Test")
                .article(123L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(9990))
                .amount(15)
                .build();
        when(productRepositoryMock.existsByArticle(createProductDto.getArticle())).thenReturn(true);

        ProductArticleAlreadyExistsException result = assertThrows(
                ProductArticleAlreadyExistsException.class,
                () -> underTest.createProduct(createProductDto)
        );

        assertEquals("Product with article ["+123L+"] already exist", result.getMessage());
        verify(productRepositoryMock).existsByArticle(createProductDto.getArticle());
    }

    @Test
    void updateProduct_Success() {
        final UpdateProductDto updateProductDto = UpdateProductDto.builder()
                .name("updated-name")
                .article(123L)
                .price(BigDecimal.valueOf(999.99))
                .amount(10)
                .build();

        final ProductEntity existingProductEntity = ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("old-name")
                .article(12L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(500.00))
                .amount(5)
                .changedAmount(LocalDateTime.now())
                .createdAt(LocalDate.now())
                .build();
        final ProductDto productDto = ProductDto.builder()
                .id(existingProductEntity.getId())
                .name("old-name")
                .article(12L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(500.00))
                .amount(5)
                .changedAmount(LocalDateTime.now())
                .createdAt(LocalDate.now())
                .build();

        when(productRepositoryMock.findById(existingProductEntity.getId())).thenReturn(Optional.of(existingProductEntity));
        when(conversionServiceMock.convert(existingProductEntity, ProductDto.class)).thenReturn(productDto);
        when(conversionServiceMock.convert(productDto, ProductEntity.class)).thenReturn(existingProductEntity);

        when(productRepositoryMock.existsByArticle(updateProductDto.getArticle())).thenReturn(false);
        when(productRepositoryMock.save(existingProductEntity)).thenReturn(existingProductEntity);

        UUID updatedProductId = underTest.updateProduct(existingProductEntity.getId(), updateProductDto);

        assertNotNull(updatedProductId);

        assertEquals(123L, existingProductEntity.getArticle());
        assertEquals("updated-name", existingProductEntity.getName());
        assertEquals(BigDecimal.valueOf(999.99), existingProductEntity.getPrice());
        assertEquals(10, existingProductEntity.getAmount());
        assertNotNull(existingProductEntity.getChangedAmount());

        verify(productRepositoryMock).save(existingProductEntity);
        verify(productRepositoryMock).existsByArticle(123L);
    }

    @Test
    void testDeleteProduct_Success() {
        final UUID ID = UUID.randomUUID();
        final ProductEntity productEntity = ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("old-name")
                .article(12L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(500.00))
                .amount(5)
                .changedAmount(LocalDateTime.now())
                .createdAt(LocalDate.now())
                .build();
        final ProductDto productDto = ProductDto.builder()
                .id(productEntity.getId())
                .name("old-name")
                .article(12L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(500.00))
                .amount(5)
                .changedAmount(LocalDateTime.now())
                .createdAt(LocalDate.now())
                .build();

        when(productRepositoryMock.findById(ID)).thenReturn(Optional.of(productEntity));
        when(conversionServiceMock.convert(productEntity, ProductDto.class)).thenReturn(productDto);
        when(conversionServiceMock.convert(productDto, ProductEntity.class)).thenReturn(productEntity);

        underTest.deleteProduct(ID);

        verify(productRepositoryMock).delete(productEntity);
    }

    @Test
    void testDeleteProduct_ProductNotFoundException() {
        UUID uuid = UUID.randomUUID();

        when(productRepositoryMock.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> underTest.deleteProduct(uuid));

        verify(productRepositoryMock, never()).delete(any(ProductEntity.class));
    }
}
