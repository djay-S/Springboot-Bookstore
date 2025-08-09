package org.demo.orderservice.domain.model.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Customer(
        @NotBlank(message = "Customer Name is required") String name,
        @NotBlank(message = "Customer email is required") @Email String email,
        @NotBlank(message = "Customer Phone Number is required") String phone
) {}
