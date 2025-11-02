package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.gate.enums.UserPermissionEnum;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.contract.business.AbstractContract;
import ru.kuznetsov.shop.represent.dto.AbstractDto;

import java.util.Collection;
import java.util.List;

import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.*;

public abstract class AbstractController<E extends AbstractDto, S extends AbstractContract<E>> {

    protected final S contractService;

    private final AuthContract authService;
    private final PermissionsConfig permissionsConfig;

    public AbstractController(S contractService, AuthContract authService, PermissionsConfig permissionsConfig) {
        this.contractService = contractService;
        this.authService = authService;
        this.permissionsConfig = permissionsConfig;
    }

    @GetMapping("/{id}")
    public ResponseEntity<E> getById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id) {
        if (hasAccess(token, GET)) {
            return ResponseEntity.ok(contractService.getById(id));
        } else return ResponseEntity.status(401).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<E>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (hasAccess(token, GET)) {
            return ResponseEntity.ok(contractService.getAll());
        } else return ResponseEntity.status(401).build();
    }

    @PostMapping("/add")
    public ResponseEntity<E> add(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody E entity) {
        if (hasAccess(token, SAVE)) {
            return ResponseEntity.ok(contractService.create(entity));
        } else return ResponseEntity.status(401).build();
    }

    @PostMapping("/add/batch")
    public ResponseEntity<Collection<E>> addBatch(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Collection<E> entity) {
        if (hasAccess(token, SAVE)) {
            return ResponseEntity.ok(contractService.createBatch(entity));
        } else return ResponseEntity.status(401).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<E> update(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id, @RequestBody E entity) {
        if (hasAccess(token, UPDATE)) {
            return new ResponseEntity("Method not implemented yet", HttpStatus.NOT_IMPLEMENTED);
        } else return ResponseEntity.status(401).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id) {
        if (hasAccess(token, UPDATE)) {
            contractService.delete(id);
            return ResponseEntity.ok().build();
        } else return ResponseEntity.status(401).build();
    }

    protected Boolean hasAccess(String token, UserPermissionEnum permission) {
        if (authService.isTokenValid(token)) {
            return authService.getUserRoles(token).stream()
                    .map(role -> permissionsConfig.hasAccess(role, permission))
                    .anyMatch(value -> value.equals(true));

        } else return false;
    }
}
