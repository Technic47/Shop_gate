package ru.kuznetsov.shop.gate.controller.business;

import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.gate.util.RequestParamUtils;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.contract.business.StoreContract;
import ru.kuznetsov.shop.represent.dto.StoreDto;

import java.util.Collection;
import java.util.Map;

import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.GET;
import static ru.kuznetsov.shop.gate.util.RequestParamUtils.*;

@RestController
@RequestMapping("/store")
public class StoreController extends AbstractController<StoreDto, StoreContract> {

    public StoreController(StoreContract contractService, AuthContract authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }

    @Override
    public ResponseEntity<Collection<StoreDto>> getAllForUser(String token, @Nullable Map<String, String> reqParam) {
        if (hasAccess(token, GET)) {
            return ResponseEntity.ok(contractService.getAll(
                    RequestParamUtils.getParamLongValue(reqParam, ID_PARAMETER),
                    RequestParamUtils.getParamStringValue(reqParam, NAME_PARAMETER),
                    RequestParamUtils.getParamLongValue(reqParam, ADDRESS_ID_PARAMETER),
                    RequestParamUtils.getParamUUIDValue(reqParam, OWNER_ID_PARAMETER)));

        } else return ResponseEntity.status(401).build();
    }
}
