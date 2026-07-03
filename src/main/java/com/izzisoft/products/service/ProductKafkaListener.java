package com.izzisoft.products.service;

import com.izzisoft.kafka.IncreaseProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductKafkaListener {

    private final ProductService productService;

    @KafkaListener(topics = "product-increase", groupId = "payment-group")
    public void increaseProductQuantityAfterFailPayment(IncreaseProductEvent increaseProductEvent) {
        productService.increaseProductQuantity(increaseProductEvent.productId(), increaseProductEvent.productAmount());
    }
}
