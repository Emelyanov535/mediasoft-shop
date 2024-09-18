package ru.mediasoft.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.mediasoft.shop.controller.dto.UpdateProductRequest;
import ru.mediasoft.shop.exception.ProductNotFoundException;
import ru.mediasoft.shop.enumeration.CategoryType;
import ru.mediasoft.shop.service.ProductService;
import ru.mediasoft.shop.service.dto.ProductDto;
import ru.mediasoft.shop.service.dto.UpdateProductDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productServiceMock;

    private final static String PRODUCT_CONTROLLER_PATH = "/api/v1/product";

    @Test
    void testFindById_Success() throws Exception {
        final UUID productId = UUID.randomUUID();
        final ProductDto productDto = ProductDto.builder()
                .id(productId)
                .name("name")
                .article(123L)
                .description("Test")
                .category(CategoryType.FOOD)
                .price(BigDecimal.valueOf(500.00))
                .amount(5)
                .changedAmount(LocalDateTime.now())
                .createdAt(LocalDate.now())
                .build();

        when(productServiceMock.findProductById(productId)).thenReturn(productDto);

        mockMvc.perform(get(PRODUCT_CONTROLLER_PATH + "/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"id\":\"" + productId + "\"," +
                        "\"name\":\"name\"," +
                        "\"article\":123," +
                        "\"description\":\"Test\"," +
                        "\"category\":\"FOOD\"," +
                        "\"price\":500.00," +
                        "\"amount\":5" +
                        "}"));
    }

    @Test
    void testFindById_ProductNotFound() throws Exception {
        final UUID productId = UUID.randomUUID();

        when(productServiceMock.findProductById(productId)).thenThrow(new ProductNotFoundException(productId));

        mockMvc.perform(get(PRODUCT_CONTROLLER_PATH + "/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Product with id [" + productId + "] is not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        UUID productId = UUID.randomUUID();
        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
                .name("Updated")
                .article(124L)
                .description("Updated")
                .category(CategoryType.CARE_PRODUCTS)
                .price(BigDecimal.valueOf(600.00))
                .amount(15)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(updateProductRequest);

        when(productServiceMock.updateProduct(any(UUID.class), any(UpdateProductDto.class))).thenReturn(productId);

        mockMvc.perform(patch(PRODUCT_CONTROLLER_PATH + "/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + productId + "\""));

        verify(productServiceMock).updateProduct(any(UUID.class), any(UpdateProductDto.class));
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(delete(PRODUCT_CONTROLLER_PATH + "/{id}", productId))
                .andExpect(status().isOk());

        verify(productServiceMock).deleteProduct(productId);
    }
}
