package com.izzisoft.kafka;

public record IncreaseProductEvent(
        Long productId,
        int productAmount
) {
}
