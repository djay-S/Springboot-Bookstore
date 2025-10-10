package org.demo.orderservice.web.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.orderservice.domain.OrderService;
import org.demo.orderservice.domain.SecurityService;
import org.demo.orderservice.domain.exception.OrderNotFoundException;
import org.demo.orderservice.domain.model.records.CreateOrderRequest;
import org.demo.orderservice.domain.model.records.CreateOrderResponse;
import org.demo.orderservice.domain.model.records.OrderDTO;
import org.demo.orderservice.domain.model.records.OrderSummary;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
/*
    With the help of `@SecurityRequirement()` we can
    add the OpenAPI Security Scheme at the class level for
    authentication and token retrieval for all the bellow endpoints.
    We can also have the same for individual endpoints,
    for which we would add the annotation at the method
    The name `security_auth` is used to get the security scheme
*/
@SecurityRequirement(name = "security_auth")
public class OrderController {

    private final OrderService orderService;
    private final SecurityService securityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        final String userName = securityService.getLoginUserName();
        log.info("Creating order for user: {}", userName);
        return orderService.createOrder(userName, request);
    }

    @GetMapping
    List<OrderSummary> getOrders() {
        String userName = securityService.getLoginUserName();
        log.info("Fetching orders for user: {}", userName);
        return orderService.findOrders(userName);
    }

    @GetMapping(value = "/{orderNumber}")
    OrderDTO getOrder(@PathVariable(value = "orderNumber") String orderNumber) {
        log.info("Fetching order by id: {}", orderNumber);
        String userName = securityService.getLoginUserName();
        return orderService
                .findUserOrder(userName, orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }
}
