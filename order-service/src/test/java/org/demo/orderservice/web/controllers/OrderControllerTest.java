package org.demo.orderservice.web.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.demo.orderservice.AbstractIntegrationTest;
import org.demo.orderservice.WithMockOAuth2User;
import org.demo.orderservice.domain.model.records.OrderSummary;
import org.demo.orderservice.testdata.TestDataGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

/*
 * For every test method this sql script is executed.
 * By default, if the file is not specified, it checks for classpath
 * */
@Sql("/test-orders.sql")
@WithMockOAuth2User(username = "user")
class OrderControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCreateNewOrderSuccessfully() throws Exception {
            mockGetProductByCode("P100", "Product 1", new BigDecimal("25.50"));
            var payload =
                    """
                        {
                            "customer" : {
                                "name": "Siva",
                                "email": "siva@gmail.com",
                                "phone": "999999999"
                            },
                            "deliveryAddress" : {
                                "addressLine1": "HNO 123",
                                "addressLine2": "Kukatpally",
                                "city": "Hyderabad",
                                "state": "Telangana",
                                "zipCode": "500072",
                                "country": "India"
                            },
                            "items": [
                                {
                                    "code": "P100",
                                    "name": "Product 1",
                                    "price": 25.50,
                                    "quantity": 1
                                }
                            ]
                        }
                    """;
            mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.orderNumber", notNullValue()))
                    .andReturn();
        }

        @Test
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() throws Exception {
            var payload = TestDataGenerator.createOrderRequestWithInvalidCustomer();
            mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.valueOf(payload)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetOrdersTest {
        @Test
        void shouldGetOrdersSuccessfully() throws Exception {
            MvcResult mvcResult = mockMvc.perform(get("/api/orders"))
                    .andExpect(status().isOk())
                    .andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
            List<OrderSummary> orderSummaries = objectMapper.readValue(contentAsString, new TypeReference<>() {});

            assertThat(orderSummaries).hasSize(2);
        }
    }

    @Nested
    class GetOrderByOrderNumberTests {
        String orderNumber = "order-123";

        @Test
        void shouldGetOrderSuccessfully() throws Exception {
            mockMvc.perform(get("/api/orders/{orderNumber}", orderNumber))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orderNumber", is(orderNumber)))
                    .andExpect(jsonPath("$.items.size()", is(2)));
        }
    }
}
