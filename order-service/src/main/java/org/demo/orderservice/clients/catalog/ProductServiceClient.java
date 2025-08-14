package org.demo.orderservice.clients.catalog;

import java.util.Optional;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductServiceClient {
    private final RestClient restClient;

/**
 * <p>
 * `@Retry` is resilience 4j annotation used to retry the request if any exception is thrown by the method.
 * This means the exception should not be consumed completely by the getProductByCode, hence the try - catch was removed
 *
 * By default @Retry will retry for 3 times and would wait for 500ms for each retry.
 * </p>
 * name: This is used to give a backend name to this retry.
 *            This is used when configuring this in application.properties<br>
 * fallbackMethod: This defines the name of the method to be called after all the retries have failed
* */
    @Retry(name = "catalog-service", fallbackMethod = "getProductByCodeFallback")
    public Optional<Product> getProductByCode(String code) {
        log.info("Fetching product for code: {}", code);
            var product = restClient
                    .get()
                    .uri("/api/products/{code}", code)
                    .retrieve()
                    .body(Product.class);
            return Optional.ofNullable(product);
    }

    Optional<Product> getProductByCodeFallback(String code, Throwable throwable) {
        log.warn("ProductServiceClient.getProductByCodeFallback: code: {}", code);
        return Optional.empty();
    }
}
