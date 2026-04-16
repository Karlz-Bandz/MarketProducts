package com.izzisoft.products.service;

import com.izzisoft.products.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    void getProductByIdTest() {
        ProductDto foundProduct = productService.getProductById(2L);
        assertEquals("iPhone 15", foundProduct.name());
    }

    @Test
    void updateProductTest() {

        ProductDto productDto = new ProductDto(
                null,
                "new name",
                "new desc",
                4,
                BigDecimal.valueOf(12.34)
        );

        productService.updateProduct(3L, productDto);

        ProductDto changedProduct = productService.getProductById(3L);

        assertEquals("new name", changedProduct.name());
    }

    @Test
    void getAllProductsTest() {
        List<ProductDto> allProducts = productService.getAllProducts();
        assertEquals(5, allProducts.size());
    }
}
