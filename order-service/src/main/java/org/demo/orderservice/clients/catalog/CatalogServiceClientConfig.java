package org.demo.orderservice.clients.catalog;

import org.demo.orderservice.ApplicationProperties;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class CatalogServiceClientConfig {
    /**
     * Spring can inject the RestClient bean from CatalogServiceClientConfig into ProductServiceClient without
     * a @Qualifier annotation because the CatalogServiceClientConfig class defines a single bean of type
     * RestClient.
     * <ol>
     *     <li>Unique Bean Identification</li>
     *     <p>When the application starts, Spring's IoC (Inversion of Control) container scans the
     *     configuration classes. It finds the CatalogServiceClientConfig and its @Bean method restClient(),
     *     which is a factory method for a RestClient object. Spring registers this as a single bean in its
     *     container with a specific type (RestClient).</p>
     *      <li>Autowiring by Type</li>
     *      <p>When Spring encounters the ProductServiceClient and its constructor-based dependency on a
     *      RestClient, it performs autowiring by type.
     *      It looks in its container for a bean that matches the requested type (RestClient).
     *      Since there is only one bean of that type available (the one defined in CatalogServiceClientConfig),
     *      Spring knows exactly which bean to inject.
     * This works seamlessly because the application's context contains a unique instance of RestClient
     * that satisfies the dependency.</p>
     * </ol>
     * <p>We would only need to use @Qualifier("restClient") in ProductServiceClient
     * if we had multiple beans of type RestClient defined in application context.
     * For example, if we had a second configuration class for another external service</p>
     * */
    @Bean
    RestClient restClient(ApplicationProperties applicationProperties) {
        return RestClient.builder()
                .baseUrl(applicationProperties.catalogServiceUrl())
                .requestFactory(ClientHttpRequestFactories
                        .get(ClientHttpRequestFactorySettings.DEFAULTS
                                .withConnectTimeout(Duration.ofSeconds(5))
                                .withReadTimeout(Duration.ofSeconds(5))))
                .build();
    }
}
