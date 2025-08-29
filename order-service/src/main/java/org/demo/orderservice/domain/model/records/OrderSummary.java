package org.demo.orderservice.domain.model.records;

import org.demo.orderservice.domain.model.enums.OrderStatus;

public record OrderSummary(String orderNumber, OrderStatus status) {
}
