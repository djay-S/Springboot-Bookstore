package org.demo.orderservice.domain;

import lombok.RequiredArgsConstructor;
import org.demo.orderservice.ApplicationProperties;
import org.demo.orderservice.domain.model.records.OrderCancelledEvent;
import org.demo.orderservice.domain.model.records.OrderCreatedEvent;
import org.demo.orderservice.domain.model.records.OrderDeliveredEvent;
import org.demo.orderservice.domain.model.records.OrderErrorEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

    public void publish(OrderCreatedEvent orderCreatedEvent) {
        send(properties.newOrdersQueue(), orderCreatedEvent);
    }

    public void publish(OrderDeliveredEvent event) {
        this.send(properties.deliveredOrdersQueue(), event);
    }

    public void publish(OrderCancelledEvent event) {
        this.send(properties.cancelledOrdersQueue(), event);
    }

    public void publish(OrderErrorEvent event) {
        this.send(properties.errorOrdersQueue(), event);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
    }
}
