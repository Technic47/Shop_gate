package ru.kuznetsov.shop.gate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.dto.auth.LoginPasswordDto;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;
import ru.kuznetsov.shop.represent.dto.auth.UserDto;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthContract authService;

    @PostMapping
    public ResponseEntity<TokenDto> getToken(@RequestBody LoginPasswordDto authHeader) {
        try {
            return ResponseEntity.ok(authService.getToken(authHeader));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/check/token")
    public ResponseEntity<Boolean> checkToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(authService.isTokenValid(token));
    }

    @PostMapping("/check/roles")
    public ResponseEntity<Collection<String>> checkRoles(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(authService.getUserRoles(token));
    }

    @PostMapping("/userInfo")
    public ResponseEntity<UserDto> getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(authService.getUserInfo(token));
    }
}
