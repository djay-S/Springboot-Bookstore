package org.demo.orderservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code Configuration} class for setting up OpenAPI (Swagger) documentation
 * for the Order Service APIs.
 * <p>
 * This class configures the API information, server URL, and defines the
 * OAuth2 Authorization Code flow for security using an external
 * Identity Provider (Keycloak in this demo).
 * </p>
 */
@Configuration
class OpenAPI3Configuration {

    /**
     * The URI of the OAuth2 token issuer, typically the base URL of the
     * Identity Provider (e.g., Keycloak).
     * Injected from the Spring environment property {@code spring.security.oauth2.resourceserver.jwt.issuer-uri}.
     */
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    String issuerUri;

    /**
     * The URL of the API Gateway through which the services are accessed.
     * This will be used as the server URL in the OpenAPI documentation.
     * Injected from the Spring environment property {@code swagger.api-gateway-url}.
     */
    @Value("${swagger.api-gateway-url}")
    String apiGatewayUrl;

    /**
     * Creates and configures the main {@link OpenAPI} bean for the application.
     * <p>
     * This setup includes:
     * <ul>
     * <li>API {@link Info} (title, description, version, contact)</li>
     * <li>A {@link Server} configured with the {@code apiGatewayUrl}</li>
     * <li>A global {@link SecurityRequirement} for 'Authorization'</li>
     * <li>An {@link OAuthFlows#authorizationCode(OAuthFlow) Authorization Code}
     * {@link SecurityScheme} named "security_auth" pointing to the
     * Keycloak/Issuer endpoints for authentication and token retrieval.</li>
     * </ul>
     * </p>
     *
     * @return A fully configured {@link OpenAPI} instance.
     */
    @Bean
    OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Service APIs")
                        .description("BookStore Order Service APIs")
                        .version("v1.0.0")
                        .contact(new Contact().name("SivaLabs").email("sivalabs@sivalabs.in")))
                .servers(List.of(new Server().url(apiGatewayUrl)))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(new Components()
                        .addSecuritySchemes(
                                "security_auth",
                                new SecurityScheme()
                                        .in(SecurityScheme.In.HEADER)
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .flows(new OAuthFlows()
                                                .authorizationCode(new OAuthFlow()
                                                        .authorizationUrl(issuerUri + "/protocol/openid-connect/auth")
                                                        .tokenUrl(issuerUri + "/protocol/openid-connect/token")
                                                        .scopes(new Scopes().addString("openid", "openid scope"))))));
    }
}