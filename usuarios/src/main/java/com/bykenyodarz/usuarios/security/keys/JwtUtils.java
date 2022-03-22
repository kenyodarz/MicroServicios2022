package com.bykenyodarz.usuarios.security.keys;

import com.bykenyodarz.usuarios.services.UserDetailsImpl;
import com.bykenyodarz.usuarios.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);
    private final UserDetailsServiceImpl repository;

    @Value("${example.app.jwtSecret}")
    private String jwtSecret;

    @Value("${example.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        var userPrincipal = repository.loadUserByUsername(username);
        var key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        var oMapper = new ObjectMapper();
        HashMap<String, Object> map = oMapper.convertValue(userPrincipal, HashMap.class);
        return Jwts.builder()
                .setClaims(map)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        var key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String token) {
        var key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (InvalidClaimException e) {
            LOGGER.error("El campo 'subject' faltaba o no tenía un valor : {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Token Invalido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("Token ha expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Token ha sido alterado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("La cadena de caracteres del JWT esta vacía: {}", e.getMessage());
        } /*catch (MissingClaimException e) {
            LOGGER.error("El JWT analizado no tenía el subcampo: {}", e.getMessage());
        } catch (IncorrectClaimException e) {
            LOGGER.error("El JWT analizado tenía un subcampo, pero su valor no era igual a 'usuario': {}", e.getMessage());
        }*/
        return false;
    }

}
