package ru.kuznetsov.shop.gate.controller.business;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.gate.util.TokenUtils;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.contract.business.StoreContract;
import ru.kuznetsov.shop.represent.dto.StockDto;
import ru.kuznetsov.shop.represent.dto.StoreDto;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.GET;

@RestController
@RequestMapping("/store")
public class StoreController extends AbstractController<StoreDto, StoreContract> {

    public StoreController(StoreContract contractService, AuthContract authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }

    @Override
    public ResponseEntity<Collection<StoreDto>> getAllForUser(String token, @Nullable Map<String, String> reqParam) {
        if (hasAccess(token, GET)) {
            UUID userId = TokenUtils.getUserIdFromToken(token);

            if (reqParam != null) {
                return ResponseEntity.ok(contractService.getAll(
                        Long.parseLong(reqParam.get("id")),
                        reqParam.get("name"),
                        Long.parseLong(reqParam.get("addressId")),
                        userId));
            } else {
                return ResponseEntity.ok(contractService.getAll(null, null, null, userId));
            }

        } else return ResponseEntity.status(401).build();
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Collection<StockDto>> getAllStockByStoreId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable long id) {
        if (hasAccess(token, GET)) {
            return ResponseEntity.ok(contractService.getAllStockByStoreId(id));
        } else return ResponseEntity.status(401).build();
    }
}
