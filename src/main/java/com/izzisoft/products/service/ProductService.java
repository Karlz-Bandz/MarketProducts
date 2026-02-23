package com.izzisoft.products.service;

import com.izzisoft.products.dto.ProductDto;
import com.izzisoft.products.exception.ProductNotFoundException;
import com.izzisoft.products.exception.ProductQuantityIsTooSmallException;
import com.izzisoft.products.model.Product;
import com.izzisoft.products.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void decreaseProductQuantity(Long productId, int quantity) {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found!")
        );

        int currentQuantity = foundProduct.getQuantity();

        if (currentQuantity >= quantity) {
            foundProduct.setQuantity(currentQuantity - quantity);
        } else {
            throw new ProductQuantityIsTooSmallException("Product quantity is too small!");
        }
    }

    @Transactional
    public void updateProduct(Long id, ProductDto productDto) {
        Product foundProduct = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not found!")
        );

        foundProduct.setName(productDto.name());
        foundProduct.setDescription(productDto.description());
        foundProduct.setQuantity(productDto.quantity());
        foundProduct.setPrice(productDto.price());
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public ProductDto getProductById(Long id) {
        Product foundProduct = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not found!")
        );

        return new ProductDto(
                foundProduct.getId(),
                foundProduct.getName(),
                foundProduct.getDescription(),
                foundProduct.getQuantity(),
                foundProduct.getPrice()
        );
    }

    public void addNewProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .quantity(productDto.quantity())
                .price(productDto.price())
                .build();

        productRepository.save(product);
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getQuantity(),
                        product.getPrice()
                ))
                .toList();
    }
}
