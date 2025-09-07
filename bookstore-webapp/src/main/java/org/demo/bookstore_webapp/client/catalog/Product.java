package org.demo.bookstore_webapp.client.catalog;

import java.math.BigDecimal;

public record Product(Long id, String code, String name, String description, String imageUrl, BigDecimal price) {}
