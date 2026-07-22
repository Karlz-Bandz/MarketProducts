package com.izzisoft.products.service;

import com.izzisoft.products.dto.ProductDto;
import com.izzisoft.products.exception.ImageFileNotFoundException;
import com.izzisoft.products.exception.ProductNoImageException;
import com.izzisoft.products.exception.ProductNotFoundException;
import com.izzisoft.products.exception.ProductQuantityIsTooSmallException;
import com.izzisoft.products.model.Product;
import com.izzisoft.products.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private static final String PATH = "uploads/images";

    public Resource getImage(Long productId) {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found!")
        );

        String imageUrl = foundProduct.getImageUrl();

        if(imageUrl == null) {
            throw new ImageFileNotFoundException("Product has no image!");
        }

        Path imagePath = Paths.get("uploads" + imageUrl);

        Resource resource = new FileSystemResource(imagePath);

        if(!resource.exists() || !resource.isReadable()) {
            throw new ProductNoImageException("Image file not found!");
        }

        return resource;
    }

    public void uploadImage(Long productId, MultipartFile file) {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found!")
        );

        try {
            if(foundProduct.getImageUrl() != null) {
                Path oldImage = Paths.get("uploads" + foundProduct.getImageUrl());
                Files.deleteIfExists(oldImage);
            }

            Path folder = Paths.get(PATH);
            Files.createDirectories(folder);

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path filePath = folder.resolve(filename);

            Files.copy(file.getInputStream(), filePath);

            String imageUrl =  "/images/" + filename;

            foundProduct.setImageUrl(imageUrl);

            productRepository.save(foundProduct);

        } catch (Exception e) {
            throw new RuntimeException("Upload image exception!");
        }
    }

    @Transactional
    public void increaseProductQuantity(Long productId, int quantity) {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found!")
        );

        int currentQuantity = foundProduct.getQuantity();
        foundProduct.setQuantity(currentQuantity + quantity);
    }

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
