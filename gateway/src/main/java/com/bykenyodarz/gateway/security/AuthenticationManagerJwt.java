package com.bykenyodarz.gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuthenticationManagerJwt implements ReactiveAuthenticationManager {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationManagerJwt.class);

    @Value("${example.app.jwtSecret}")
    private String jwtSecret;


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication.getCredentials().toString())
                .map(token -> {
                    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
                    return Jwts
                            .parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
                })
                .map(claims -> {
                    String username = claims.get("username", String.class);
                    List<Map<String, String>> roles = claims.get("authorities", List.class);
                    Collection<GrantedAuthority> authorities = roles.stream().map(role ->
                                    new SimpleGrantedAuthority(role.get("authority")))
                            .collect(Collectors.toList());
                    log.info("Authorities: {}", authorities);
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                });
    }
}
