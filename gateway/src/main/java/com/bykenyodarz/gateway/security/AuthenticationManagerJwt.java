package com.bykenyodarz.gateway.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
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

    public boolean validateJwtToken(String token) {
        var key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (InvalidClaimException e) {
            log.error("El campo 'subject' faltaba o no tenía un valor : {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Token Invalido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Token ha expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Token ha sido alterado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("La cadena de caracteres del JWT esta vacía: {}", e.getMessage());
        } /*catch (MissingClaimException e) {
            log.error("El JWT analizado no tenía el subcampo: {}", e.getMessage());
        } catch (IncorrectClaimException e) {
            log.error("El JWT analizado tenía un subcampo, pero su valor no era igual a 'usuario': {}", e.getMessage());
        }*/
        return false;
    }
}
