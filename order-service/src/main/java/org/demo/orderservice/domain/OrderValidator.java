package org.demo.orderservice.domain;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.orderservice.clients.catalog.Product;
import org.demo.orderservice.clients.catalog.ProductServiceClient;
import org.demo.orderservice.domain.exception.InvalidOrderException;
import org.demo.orderservice.domain.model.records.CreateOrderRequest;
import org.demo.orderservice.domain.model.records.OrderItem;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderValidator {
    private final ProductServiceClient productServiceClient;

    void validate(CreateOrderRequest request) {
        Set<OrderItem> items = request.items();
        for (var item : items) {
            Product product = productServiceClient
                    .getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid Product code: " + item.code()));

            // Since new BigDecimal("25.50").equals(new BigDecimal("25.500")) == false and new
            // BigDecimal("25.50").compareTo(new BigDecimal("25.500")) == 0
            if (item.price().compareTo(product.price()) != 0) {
                log.error(
                        "Product price not matching. Actual price: {}, received price: {}",
                        product.price(),
                        item.price());
                throw new InvalidOrderException("Product price not matching");
            }
        }
    }
}
