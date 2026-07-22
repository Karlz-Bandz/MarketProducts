package com.izzisoft.products.controller;

import com.izzisoft.products.dto.ProductDto;
import com.izzisoft.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getImageName(@PathVariable("id") Long id) {

        Resource image = productService.getImage(id);

        MediaType mediaType = MediaTypeFactory
                .getMediaType(image)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(image);
    }

    @PostMapping("/{id}/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> uploadProductImage(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        productService.uploadImage(id, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/increase/{id}/{quantity}")
    @PreAuthorize("hasRole('SERVICE')")
    public ResponseEntity<Void> increaseProductQuantity(@PathVariable("id") Long id, @PathVariable("quantity") int quantity) {
        productService.increaseProductQuantity(id, quantity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/decrease/{id}/{quantity}")
    @PreAuthorize("hasRole('SERVICE')")
    public ResponseEntity<Void> decreaseProductQuantity(@PathVariable("id") Long id, @PathVariable("quantity") int quantity) {
        productService.decreaseProductQuantity(id, quantity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDto productDto) {
        productService.updateProduct(id, productDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addNewProduct(@RequestBody ProductDto productDto) {
        productService.addNewProduct(productDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
}
