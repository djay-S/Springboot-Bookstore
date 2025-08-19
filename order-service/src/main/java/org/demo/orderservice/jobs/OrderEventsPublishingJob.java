package org.demo.orderservice.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.orderservice.domain.OrderEventService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
class OrderEventsPublishingJob {
    private final OrderEventService orderEventService;

    @Scheduled(cron = "${orders.publish-order-events-job-cron}")
    public void publishOrderEvents() {
        log.info("Publishing Order Events at {}", Instant.now());
        orderEventService.publishOrderEvents();
    }
}
