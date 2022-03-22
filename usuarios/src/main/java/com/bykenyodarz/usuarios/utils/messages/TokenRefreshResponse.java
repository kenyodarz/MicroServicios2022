package com.bykenyodarz.usuarios.utils.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenRefreshResponse {
  private static final String TOKEN_TYPE = "Bearer";
  private String accessToken;
  private String refreshToken;
}
