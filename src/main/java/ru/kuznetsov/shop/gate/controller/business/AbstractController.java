package ru.kuznetsov.shop.gate.controller.business;

import jakarta.annotation.Nullable;
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
import java.util.Map;
import java.util.UUID;

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

    @GetMapping()
    public abstract ResponseEntity<Collection<E>> getAllForUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Nullable @RequestParam(required = false) Map<String, String> reqParam);

    @GetMapping("/bulk")
    public ResponseEntity<Collection<E>> getAllBulk(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return getAllBulkInternal(token, GET_ALL_BULK);
    }

    @PostMapping()
    public ResponseEntity<E> add(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody E entity) {
        return addInternal(token, entity, SAVE);
    }

    @PostMapping("/batch")
    public ResponseEntity<Collection<E>> addBatch(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Collection<E> entity) {
        return addBatchInternal(token, entity, SAVE);
    }

    @PutMapping("/{id}")
    public ResponseEntity<E> update(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id, @RequestBody E entity) {
        return updateInternal(token, id, entity, UPDATE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id) {
        return deleteInternal(token, id, DELETE);
    }

    protected Boolean hasAccess(String token, UserPermissionEnum permission) {
        if (authService.isTokenValid(token)) {
            return authService.getUserRoles(token).stream()
                    .map(role -> permissionsConfig.hasAccess(role, permission))
                    .anyMatch(value -> value.equals(true));

        } else return false;
    }

    protected UUID getUserIdFromToken(String token) {
        return authService.getUserInfo(token).getId();
    }

    protected ResponseEntity<Collection<E>> getAllBulkInternal(String token, UserPermissionEnum permission) {
        if (hasAccess(token, permission)) {
            return ResponseEntity.ok(contractService.getAll());
        } else return ResponseEntity.status(401).build();
    }

    protected ResponseEntity<E> addInternal(String token, E entity, UserPermissionEnum permission) {
        if (hasAccess(token, permission)) {
            return ResponseEntity.ok(contractService.create(entity));
        } else return ResponseEntity.status(401).build();
    }

    protected ResponseEntity<Collection<E>> addBatchInternal(String token, Collection<E> entity, UserPermissionEnum permission) {
        if (hasAccess(token, permission)) {
            return ResponseEntity.ok(contractService.createBatch(entity));
        } else return ResponseEntity.status(401).build();
    }

    protected ResponseEntity<E> updateInternal(String token, Long id, E entity, UserPermissionEnum permission) {
        if (hasAccess(token, permission)) {
            return new ResponseEntity("Method not implemented yet", HttpStatus.NOT_IMPLEMENTED);
        } else return ResponseEntity.status(401).build();
    }

    protected ResponseEntity<Collection<E>> deleteInternal(String token, Long id, UserPermissionEnum permission) {
        if (hasAccess(token, permission)) {
            contractService.delete(id);
            return ResponseEntity.ok().build();
        } else return ResponseEntity.status(401).build();
    }
}
