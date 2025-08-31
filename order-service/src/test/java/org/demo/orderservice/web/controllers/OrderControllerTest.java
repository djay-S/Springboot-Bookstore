package org.demo.orderservice.web.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.List;

import org.demo.orderservice.AbstractIntegrationTest;
import org.demo.orderservice.domain.model.records.OrderSummary;
import org.demo.orderservice.testdata.TestDataGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

/*
* For every test method this sql script is executed.
* By default, if the file is not specified, it checks for classpath
* */
@Sql("/test-orders.sql")
class OrderControllerTest extends AbstractIntegrationTest {

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCreateNewOrderSuccessfully() {
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
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
            var payload = TestDataGenerator.createOrderRequestWithInvalidCustomer();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class GetOrdersTest {
        @Test
        void shouldGetOrdersSuccessfully() {
            List<OrderSummary> orderSummaries = given().when()
                    .get("/api/orders")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .body()
                    .as(new TypeRef<>() {
                    });

            assertThat(orderSummaries).hasSize(2);
        }
    }

    @Nested
    class GetOrderByOrderNumberTests {
        String orderNumber = "order-123";

        @Test
        void shouldGetOrderSuccessfully() {
            given().when()
                    .get("/api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("orderNumber", is(orderNumber))
                    .body("items.size()", is(2));
        }
    }
}
