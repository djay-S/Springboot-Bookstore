package org.demo.catalog_service.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
        // By default, DataJpaTest uses H2 database for testing.
        // Since we are using test containers we define our test container
        properties = {
            //                This tells not to create DB instance with in memory settings
            "spring.test.database.replace=none",
            //                This defines to use our test container db
            "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db",
        })
@Sql("/test-data.sql")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    //    You don't need to test the methods provided by Spring Data JPA.
    //    This test is to demonstrate how to write tests for the repository layer.
    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        assertThat(productEntities).hasSize(15);
    }

    @Test
    void shouldGetProductByCode() {
        ProductEntity productEntity = productRepository.findByCode("P101").orElseThrow();
        assertThat(productEntity.getCode()).isEqualTo("P101");
        assertThat(productEntity.getName()).isEqualTo("To Kill a Mockingbird");
        assertThat(productEntity.getDescription()).isEqualTo("The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it...");
        assertThat(productEntity.getPrice()).isEqualTo(new BigDecimal("45.40"));
    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotExist() {
        Optional<ProductEntity> productEntity = productRepository.findByCode("invalid_product_code");
        assertThat(productEntity).isEmpty();
    }
}
