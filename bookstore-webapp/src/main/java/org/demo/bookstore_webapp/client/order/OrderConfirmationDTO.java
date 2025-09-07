package org.demo.bookstore_webapp.client.order;

public record OrderConfirmationDTO(String orderNumber, OrderStatus status) {}
