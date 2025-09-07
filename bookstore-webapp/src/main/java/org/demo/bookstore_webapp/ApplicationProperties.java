package org.demo.bookstore_webapp;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Extra comment to trigger build
@ConfigurationProperties(prefix = "bookstore")
public record ApplicationProperties(String apiGatewayUrl) {}
