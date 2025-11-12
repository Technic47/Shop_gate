package ru.kuznetsov.shop.gate.controller.business.order;

import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.gate.controller.business.AbstractController;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.contract.order.OrderContract;
import ru.kuznetsov.shop.represent.dto.order.OrderDto;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.GET;

@RestController
@RequestMapping("/order")
public class OrderController extends AbstractController<OrderDto, OrderContract> {

    public OrderController(OrderContract contractService, AuthContract authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }

    @Override
    public ResponseEntity<Collection<OrderDto>> getAllForUser(String token, @Nullable Map<String, String> reqParam) {
        if (hasAccess(token, GET)) {
            UUID userId = getUserIdFromToken(token);

            return ResponseEntity.ok(contractService.getAllByCustomerId(userId));
        } else return ResponseEntity.status(401).build();
    }
}
