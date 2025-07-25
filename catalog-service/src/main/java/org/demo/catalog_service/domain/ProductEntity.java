package org.demo.catalog_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table(name = "PRODUCTS")
@Entity
class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
    @SequenceGenerator(name = "product_id_generator", sequenceName = "product_id_seq")
    private long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Product code is required")
    private String code;

    @Column(nullable = false)
    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    private String imageUrl;

    @Column(nullable = false)
    @NotNull(message = "Product price is required")
    @DecimalMin("0.1")
    private BigDecimal price;
}
