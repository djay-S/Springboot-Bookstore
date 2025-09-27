package org.demo.bookstore_webapp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Helper service for accessing security-related information, specifically for retrieving the
 * OAuth 2.0 access token from the current user's security context.
 * <p>
 * This class is marked with {@code @Service}, making it a Spring-managed component
 * that can be easily injected into other services or controllers.
 * It uses {@code @RequiredArgsConstructor} to automatically create a constructor
 * for its final field, {@code oAuth2AuthorizedClientService}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class SecurityHelper {
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    /**
     * Retrieves the OAuth 2.0 access token for the currently authenticated user.
     * <p>
     * This method accesses the current security context via {@link SecurityContextHolder}.
     * It checks if the authentication object is an instance of {@link OAuth2AuthenticationToken}.
     * If it is, it loads the corresponding {@link OAuth2AuthorizedClient} and
     * extracts the access token value.
     * </p>
     * <p>
     * This is particularly useful for making secure API calls to external resources
     * that require an access token.
     * </p>
     *
     * @return The access token as a {@link String}, or {@code null} if the current authentication
     * is not an OAuth2AuthenticationToken or the access token cannot be found.
     */
    public String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken)) {
            return null;
        }
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient(
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());

        return oAuth2AuthorizedClient.getAccessToken().getTokenValue();
    }
}
