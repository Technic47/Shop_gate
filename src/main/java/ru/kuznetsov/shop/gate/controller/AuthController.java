package ru.kuznetsov.shop.gate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.gate.dto.LoginPasswordDto;
import ru.kuznetsov.shop.gate.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<String> auth(@RequestBody LoginPasswordDto authHeader) {
        return ResponseEntity.ok(authService.loginAuth(authHeader));
    }
}
