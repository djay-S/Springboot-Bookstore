package org.demo.orderservice.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.orderservice.domain.model.records.CreateOrderRequest;
import org.demo.orderservice.domain.model.records.CreateOrderResponse;
import org.demo.orderservice.domain.model.records.OrderCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderEventService orderEventService;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request) {
        orderValidator.validate(request);
        OrderEntity newOrder = OrderMapper.toEntity(request);
        newOrder.setUserName(userName);
        OrderEntity savedOrder = orderRepository.save(newOrder);
        log.info("Created order with orderNumber: {}", savedOrder.getOrderNumber());
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(savedOrder);
        orderEventService.save(orderCreatedEvent);
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}
