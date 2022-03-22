package com.bykenyodarz.usuarios.utils.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private static final String TOKEN_TYPE = "Bearer";
    private String token;
    private String refreshToken;
    private String id;
    private String name;
    private String username;
    private String email;
    private List<String> roles;
}
