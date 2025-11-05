package ru.kuznetsov.shop.gate.controller.business;

import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.gate.util.TokenUtils;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.contract.business.StockContract;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.GET;

@RestController
@RequestMapping("/stock")
public class StockController extends AbstractController<StockDto, StockContract> {

    public StockController(StockContract contractService, AuthContract authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }

    @Override
    public ResponseEntity<Collection<StockDto>> getAllForUser(String token, @Nullable Map<String, String> reqParam) {
        if (hasAccess(token, GET)) {
            UUID userId = TokenUtils.getUserIdFromToken(token);

            if (reqParam != null) {
                return ResponseEntity.ok(contractService.getAll(
                        Long.parseLong(reqParam.get("productId")),
                        Long.parseLong(reqParam.get("storeId")),
                        userId));
            } else {
                return ResponseEntity.ok(contractService.getAll(null, null, userId));
            }

        } else return ResponseEntity.status(401).build();
    }
}
