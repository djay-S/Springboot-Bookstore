package org.demo.notificationservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.notificationservice.domain.NotificationService;
import org.demo.notificationservice.domain.OrderEventEntity;
import org.demo.notificationservice.domain.OrderEventRepository;
import org.demo.notificationservice.domain.records.OrderCancelledEvent;
import org.demo.notificationservice.domain.records.OrderCreatedEvent;
import org.demo.notificationservice.domain.records.OrderDeliveredEvent;
import org.demo.notificationservice.domain.records.OrderErrorEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventHandler {

    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    public void handle(OrderCreatedEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderCreatedEvent with eventId: {}", event.eventId());
            return;
        }
        log.info("Received a OrderCreatedEvent with orderNumber:{}: ", event.orderNumber());
        notificationService.sendOrderCreatedNotification(event);
        var orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    public void handle(OrderDeliveredEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderDeliveredEvent with eventId: {}", event.eventId());
            return;
        }
        log.info("Received a OrderDeliveredEvent with orderNumber:{}: ", event.orderNumber());
        notificationService.sendOrderDeliveredNotification(event);
        var orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    public void handle(OrderCancelledEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderCancelledEvent with eventId: {}", event.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        log.info("Received a OrderCancelledEvent with orderNumber:{}: ", event.orderNumber());
        var orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    public void handle(OrderErrorEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderErrorEvent with eventId: {}", event.eventId());
            return;
        }
        log.info("Received a OrderErrorEvent with orderNumber:{}: ", event.orderNumber());
        notificationService.sendOrderErrorEventNotification(event);
        OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }
}
