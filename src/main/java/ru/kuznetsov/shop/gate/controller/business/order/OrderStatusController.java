package ru.kuznetsov.shop.gate.controller.business.order;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.gate.controller.business.AbstractController;
import ru.kuznetsov.shop.gate.util.RequestParamUtils;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.contract.order.OrderStatusContract;
import ru.kuznetsov.shop.represent.dto.order.OrderStatusDto;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.GET;

@RestController
@RequestMapping("/order/status")
public class OrderStatusController extends AbstractController<OrderStatusDto, OrderStatusContract> {

    public OrderStatusController(OrderStatusContract contractService, AuthContract authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }

    @Override
    public ResponseEntity<Collection<OrderStatusDto>> getAllForUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Nullable Map<String, String> reqParam) {
        if (hasAccess(token, GET)) {
            Long orderId = RequestParamUtils.getParamLongValue(reqParam, "orderId");

            if (orderId != null) {
                return ResponseEntity.ok(contractService.getAllByOrderId(orderId));
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else return ResponseEntity.status(401).build();
    }

    @GetMapping("/last")
    public ResponseEntity<OrderStatusDto> getLast(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam("orderId") Long orderId) {
        if (hasAccess(token, GET)) {
            Optional<OrderStatusDto> lastByOrderId = contractService.getLastByOrderId(orderId);

            return lastByOrderId.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(204).build());
        } else return ResponseEntity.status(401).build();
    }
}
