package org.demo.bookstore_webapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * <p><b>{@code SecurityConfig}</b> is a Spring Security configuration class that defines the security
 * rules and filters for the application. It enables web security and configures authentication
 * using OAuth 2.0 with OpenID Connect (OIDC).</p>
 *
 * <p>This configuration class uses a stateless approach to security, which is ideal for
 * RESTful APIs, and includes configurations for:</p>
 * <ul>
 * <li><b>Authorization:</b> Specifies which endpoints are publicly accessible and which require
 * authentication.</li>
 * <li><b>CORS & CSRF:</b> Disables Cross-Origin Resource Sharing (CORS) and Cross-Site Request
 * Forgery (CSRF) protections, which is common for applications where a separate front-end
 * serves content from a different origin.</li>
 * <li><b>OAuth 2.0 Login:</b> Configures OAuth 2.0 login with default settings, allowing
 * users to authenticate via a third-party identity provider.</li>
 * <li><b>Logout:</b> Sets up a custom logout handler to correctly manage OIDC-based
 * logout flows, including redirection to the identity provider.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ClientRegistrationRepository clientRegistrationRepository;

    /**
     * Configures the security filter chain that defines the authorization rules and security settings
     * for incoming HTTP requests.
     *
     * <p>This method builds a {@link SecurityFilterChain} with the following rules:</p>
     * <ul>
     * <li><b>Public Endpoints:</b> Grants public access to static resources (CSS, JS, images),
     * error pages, and specific API endpoints like {@code /actuator/**} and {@code /products/**}.</li>
     * <li><b>Protected Endpoints:</b> Requires authentication for all other requests that are not
     * explicitly permitted.</li>
     * <li><b>CORS & CSRF:</b> Disables CORS and CSRF protection.</li>
     * <li><b>OAuth 2.0:</b> Configures OAuth 2.0 login with default settings.</li>
     * <li><b>Logout:</b> Configures a custom logout handler to ensure a proper OIDC logout flow.</li>
     * </ul>
     *
     * @param httpSecurity The {@link HttpSecurity} object to configure.
     * @return The configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(c -> c.requestMatchers(
                                "/",
                                "/js/*",
                                "/css/*",
                                "/images/*",
                                "/error",
                                "/webjars/**",
                                "/actuator/**",
                                "/products/**",
                                "/api/products/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .cors(CorsConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout.clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler(oidcLogoutSuccessHandler()));
        return httpSecurity.build();
    }

    /**
     * Creates and configures a custom {@link LogoutSuccessHandler} for OIDC-based logout.
     *
     * <p>This handler is responsible for initiating the logout process with the OpenID Provider
     * and redirecting the user to a specific URI after a successful logout. The redirect URI is
     * set to the base URL of the application.</p>
     *
     * @return A configured {@link OidcClientInitiatedLogoutSuccessHandler}.
     */
    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
        return oidcLogoutSuccessHandler;
    }
}
