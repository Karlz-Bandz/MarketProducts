package com.izzisoft.products.dto;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String name,
        String description,
        int quantity,
        BigDecimal price
) {
}
