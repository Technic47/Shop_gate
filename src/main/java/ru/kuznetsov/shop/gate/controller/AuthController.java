package ru.kuznetsov.shop.gate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.gate.dto.LoginPasswordDto;
import ru.kuznetsov.shop.gate.dto.TokenDto;
import ru.kuznetsov.shop.gate.service.AuthService;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<TokenDto> getToken(@RequestBody LoginPasswordDto authHeader) {
        return authService.getToken(authHeader);
    }

    @PostMapping("/check/token")
    public ResponseEntity<Boolean> checkToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(authService.isTokenValid(token));
    }

    @PostMapping("/check/roles")
    public ResponseEntity<Collection<String>> checkRoles(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(authService.getUserRoles(token));
    }
}
