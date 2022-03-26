package com.bykenyodarz.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SpringSecurityConfig {

    private final JwtAuthenticationFilter authenticationFilter;

    public SpringSecurityConfig(JwtAuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/api/usuarios/auth/**").permitAll()
                .pathMatchers("/api/usuarios/test/**").permitAll()
                .pathMatchers("/api/items/**")
                .hasAnyAuthority("ROLE_USER", "ROLE_SUPERVISOR", "ROLE_ADMIN", "ROLE_MODERATOR")
                .pathMatchers(HttpMethod.GET,
                        "/api/productos/api/v1/productos",
                        "/api/productos/api/v1/productos/{id}",
                        "/api/items/{id}",
                        "/api/items/all"
                )
                .hasAnyAuthority("ROLE_USER", "ROLE_SUPERVISOR", "ROLE_ADMIN", "ROLE_MODERATOR")
                .pathMatchers("/api/productos/**")
                .hasAnyAuthority("ROLE_ADMIN")
                .anyExchange().authenticated()
                .and().addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable()
                .build();
    }

}
