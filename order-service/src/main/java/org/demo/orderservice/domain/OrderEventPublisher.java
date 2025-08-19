package org.demo.orderservice.domain;

import lombok.RequiredArgsConstructor;
import org.demo.orderservice.ApplicationProperties;
import org.demo.orderservice.domain.model.records.OrderCreatedEvent;
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

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
    }
}
