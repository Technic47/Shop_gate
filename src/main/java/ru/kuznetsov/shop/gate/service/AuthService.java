package ru.kuznetsov.shop.gate.service;

import org.springframework.http.ResponseEntity;
import ru.kuznetsov.shop.gate.dto.LoginPasswordDto;
import ru.kuznetsov.shop.gate.dto.TokenDto;

import java.util.Collection;

public interface AuthService {

    ResponseEntity<TokenDto> getToken(LoginPasswordDto authHeader);

    Boolean isTokenValid(String token);

    Collection<String> getUserRoles(String token);
}
