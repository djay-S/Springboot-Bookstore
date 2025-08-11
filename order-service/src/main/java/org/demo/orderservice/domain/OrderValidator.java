package org.demo.orderservice.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.orderservice.clients.catalog.Product;
import org.demo.orderservice.clients.catalog.ProductServiceClient;
import org.demo.orderservice.domain.exception.InvalidOrderException;
import org.demo.orderservice.domain.model.records.CreateOrderRequest;
import org.demo.orderservice.domain.model.records.OrderItem;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderValidator {
    private final ProductServiceClient productServiceClient;

    void validate(CreateOrderRequest request) {
        Set<OrderItem> items = request.items();
        for (var item : items) {
            Product product = productServiceClient.getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid Product code: " + item.code()));

            if (!item.price().equals(product.price())) {
                log.error("Product price not matching. Actual price: {}, received price: {}",
                        product.price(), item.price());
                throw new InvalidOrderException("Product price not matching");
            }
        }
    }
}
