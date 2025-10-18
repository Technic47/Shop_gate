package ru.kuznetsov.shop.gate.controller;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.gate.service.Keycloakservice;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final Keycloakservice keycloakservice;

    @GetMapping("/user")
    public ResponseEntity<List<UserRepresentation>> principal(@RequestParam String userName) {
        return ResponseEntity.ok(keycloakservice.getInfo(userName));
    }
}
