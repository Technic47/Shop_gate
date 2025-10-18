package ru.kuznetsov.shop.gate.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest implements Serializable {
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
    private String grantType;
}
