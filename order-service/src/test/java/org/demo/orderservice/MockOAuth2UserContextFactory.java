package org.demo.orderservice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.StringUtils;

public class MockOAuth2UserContextFactory implements WithSecurityContextFactory<WithMockOAuth2User> {
    @Override
    public SecurityContext createSecurityContext(WithMockOAuth2User withUser) {
        String userName = StringUtils.hasLength(withUser.username()) ? withUser.username() : withUser.value();
        if (userName == null) {
            throw new IllegalArgumentException(
                    withUser + " cannot have null username on both username and value parameters");
        }

        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (String role : withUser.roles()) {
            if (role.startsWith("ROLE_")) {
                throw new IllegalArgumentException("roles cannot start with 'ROLE_', got: " + role);
            }
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        Map<String, Object> claims = Map.of(
                "preferred_username", withUser.username(), "userId", withUser.id(), "realm_access", authorityList);
        Map<String, Object> headers = Map.of("header", "mock");
        Jwt jwt = new Jwt("mock-jwt-token", Instant.now(), Instant.now().plusSeconds(300), headers, claims);
        Authentication authentication = new JwtAuthenticationToken(jwt, authorityList);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}
