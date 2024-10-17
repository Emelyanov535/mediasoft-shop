package ru.mediasoft.shop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import ru.mediasoft.shop.controller.dto.CriteriaData;
import ru.mediasoft.shop.enumeration.CategoryType;
import ru.mediasoft.shop.persistence.entity.ProductEntity;
import ru.mediasoft.shop.persistence.repository.ProductRepository;
import ru.mediasoft.shop.service.criteriaDataType.CriteriaDataBigDecimal;
import ru.mediasoft.shop.service.criteriaDataType.CriteriaDataCategory;
import ru.mediasoft.shop.service.criteriaDataType.CriteriaDataString;
import ru.mediasoft.shop.service.dto.ProductDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static ru.mediasoft.shop.enumeration.OperationEnum.*;

@DataJpaTest
@ActiveProfiles("local")
public class SearchServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private ConversionService conversionServiceMock;

    private SearchService searchService;

    @BeforeEach
    void setUp() {
        searchService = new SearchService(productRepository, conversionServiceMock);

        final ProductEntity product1 = ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("Product1")
                .article(1L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(90))
                .amount(12)
                .changedAmount(LocalDateTime.now())
                .createdAt(LocalDate.now())
                .build();

        final ProductEntity product2 = ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("Product2")
                .article(2L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(200))
                .amount(7)
                .changedAmount(LocalDateTime.now())
                .createdAt(LocalDate.now())
                .build();

        final ProductEntity product3 = ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("Product3")
                .article(3L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(300))
                .amount(3)
                .changedAmount(LocalDateTime.now())
                .createdAt(LocalDate.now())
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        when(conversionServiceMock.convert(any(ProductEntity.class), eq(ProductDto.class)))
                .thenAnswer(invocation -> {
                    ProductEntity entity = invocation.getArgument(0);
                    return ProductDto.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .article(entity.getArticle())
                            .description(entity.getDescription())
                            .category(entity.getCategory())
                            .price(entity.getPrice())
                            .amount(entity.getAmount())
                            .changedAmount(entity.getChangedAmount())
                            .createdAt(entity.getCreatedAt())
                            .build();
                });
    }

    @Test
    void givenMultipleCriteria_whenSearchProducts_thenReturnResults() {
        Pageable pageable = PageRequest.of(0, 10);

        CriteriaData<String> criteriaName = new CriteriaDataString("name", "Product", LIKE);
        CriteriaData<CategoryType> criteriaCategory = new CriteriaDataCategory("category", CategoryType.FOOD, EQUAL);
        CriteriaData<BigDecimal> criteriaPrice = new CriteriaDataBigDecimal("price", BigDecimal.valueOf(100), GRATER_THAN_OR_EQ);

        List<CriteriaData<?>> criteriaList = List.of(criteriaName, criteriaCategory, criteriaPrice);

        List<ProductDto> result = searchService.hardSearch(pageable, criteriaList).getContent();

        assertThat(result)
                .anySatisfy(productDto -> {
                    assertEquals("Product2", productDto.getName());
                    assertEquals(CategoryType.FOOD, productDto.getCategory());
                    assertThat(productDto.getPrice()).isGreaterThan(BigDecimal.valueOf(100));
                })
                .anySatisfy(productDto -> {
                    assertEquals("Product3", productDto.getName());
                    assertEquals(CategoryType.FOOD, productDto.getCategory());
                    assertThat(productDto.getPrice()).isGreaterThan(BigDecimal.valueOf(300));
                })
                .hasSize(2);
    }
}
