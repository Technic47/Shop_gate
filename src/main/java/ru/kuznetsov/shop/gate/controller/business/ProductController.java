package ru.kuznetsov.shop.gate.controller.business;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.contract.business.ProductContract;
import ru.kuznetsov.shop.represent.dto.ProductDto;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.GET_ALL;

@RestController
@RequestMapping("/product")
public class ProductController extends AbstractController<ProductDto, ProductContract> {

    public ProductController(ProductContract contractService, AuthContract authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }

    public ResponseEntity<Collection<ProductDto>> getAllForUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Nullable @RequestParam(required = false) Map<String, String> reqParam) {
        if (hasAccess(token, GET_ALL)) {
            UUID userId = getUserIdFromToken(token);
            return ResponseEntity.ok(contractService.getAllByOwnerId(userId));
        } else return ResponseEntity.status(401).build();
    }
}
