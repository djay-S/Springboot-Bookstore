package org.demo.orderservice.web.controllers;

import static org.junit.jupiter.api.Named.named;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.demo.orderservice.domain.OrderService;
import org.demo.orderservice.domain.SecurityService;
import org.demo.orderservice.domain.model.records.CreateOrderRequest;
import org.demo.orderservice.testdata.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit tests for the {@link OrderController} using MockMvc.
 * This class focuses on testing the controller's behavior in isolation
 * by mocking its dependencies, such as {@link OrderService} and {@link SecurityService}.
 */
@WebMvcTest(OrderController.class)
public class OrderControllerUnitTest {
    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private SecurityService securityService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        given(securityService.getLoginUserName()).willReturn("djay");
    }

    static Stream<Arguments> createOrderRequestProvider() {
        return Stream.of(
                Arguments.arguments(named(
                        "Order with Invalid Customer", TestDataGenerator.createOrderRequestWithInvalidCustomer())),
                Arguments.arguments(named(
                        "Order with Invalid Delivery Address",
                        TestDataGenerator.createOrderRequestWithInvalidDeliveryAddress())),
                Arguments.arguments(named("Order with No Items", TestDataGenerator.createOrderRequestWithNoItems())));
    }

    /**
     * This test is a **Parameterized Test**, meaning it will be executed multiple times,
     * once for each set of arguments provided by a source. The source for this test
     * is specified by the {@link MethodSource} annotation.
     * The **{@link MethodSource}** annotation points to the static method
     * {@code createOrderRequestProvider}, which provides the different test cases.
     * This allows for a clean separation of test data from the test logic.
     *
     * @param request The invalid {@link CreateOrderRequest} payload to be tested.
     * @throws Exception if an error occurs during the mock MVC request.
     */
    @ParameterizedTest(name = "[{index}]-{0}")
    @MethodSource("createOrderRequestProvider")
    void shouldReturnBadRequestWhenOrderPayloadIsInvalid(CreateOrderRequest request) throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }
}
