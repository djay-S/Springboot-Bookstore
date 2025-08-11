package org.demo.orderservice.clients.catalog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductServiceClient {
    private final RestClient restClient;

    public Optional<Product> getProductByCode(String code) {
        log.info("Fetching product for code: {}", code);
        var product = restClient
                .get()
                .uri("/api/products/{code}", code)
                .retrieve()
                .body(Product.class);
        return Optional.ofNullable(product);
    }
}
