package org.demo.orderservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.orderservice.domain.model.enums.OrderEventType;
import org.demo.orderservice.domain.model.records.OrderCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderEventService {
    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;

    void save(OrderCreatedEvent event) {
        OrderEventEntity orderEventEntity = new OrderEventEntity();
        orderEventEntity.setEventId(event.eventId());
        orderEventEntity.setEventType(OrderEventType.ORDER_CREATED);
        orderEventEntity.setOrderNumber(event.orderNumber());
        orderEventEntity.setCreatedAt(event.createdAt());
        orderEventEntity.setPayload(toJsonPayload(event));
        orderEventRepository.save(orderEventEntity);
    }

    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
