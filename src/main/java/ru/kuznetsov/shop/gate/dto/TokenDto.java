package ru.kuznetsov.shop.gate.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private String token;
    private long expiresIn;
    private long refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
    private String sessionState;
    private Map<String, Object> otherClaims = new HashMap<>();
    private String scope;
    private String error;
    private String errorDescription;
}
