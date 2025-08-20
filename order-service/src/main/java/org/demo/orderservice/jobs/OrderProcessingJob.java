package org.demo.orderservice.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.orderservice.domain.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProcessingJob {
    private final OrderService orderService;

    @Scheduled(cron = "${orders.new-orders-job-cron}")
    public void processNewOrders() {
        log.info("Processing new orders at: {}", Instant.now());
        orderService.processNewOrders();
    }
}
