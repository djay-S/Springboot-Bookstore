package org.demo.catalog_service.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
//By default, DataJpaTest uses H2 database for testing.
//Since we are using test containers we define our test container
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
}