package org.demo.orderservice.domain.model.records;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record CreateOrderRequest(
        @NotEmpty(message = "Items cannot be empty")Set<OrderItem> items,
//        @Valid would trigger java bean validations for Customer record
        @Valid Customer customer,
        @Valid Address deliveryAddress
        ) {
}
