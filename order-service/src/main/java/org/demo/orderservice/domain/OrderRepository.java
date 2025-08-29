package org.demo.orderservice.domain;

import java.util.List;
import java.util.Optional;
import org.demo.orderservice.domain.model.enums.OrderStatus;
import org.demo.orderservice.domain.model.records.OrderSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus orderStatus) {
        OrderEntity order = findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(orderStatus);
        save(order);
    }

    @Query("""
            select new org.demo.orderservice.domain.model.records.OrderSummary(o.orderNumber, o.status)
            from OrderEntity o
            where o.userName = :userName
            """)
    List<OrderSummary> findByUserName(String userName);

    @Query("""
            select distinct o
            from OrderEntity o left join fetch o.items
            where o.userName = :userName and o.orderNumber = :orderNumber
            """)
    Optional<OrderEntity> findByUserNameAndOrderNumber(String userName, String orderNumber);
}
