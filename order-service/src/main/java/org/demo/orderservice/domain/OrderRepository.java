package org.demo.orderservice.domain;

import java.util.List;
import java.util.Optional;
import org.demo.orderservice.domain.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus orderStatus) {
        OrderEntity order = findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(orderStatus);
        save(order);
    }
}
