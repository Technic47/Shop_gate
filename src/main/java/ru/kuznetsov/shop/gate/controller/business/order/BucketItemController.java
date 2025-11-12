package ru.kuznetsov.shop.gate.controller.business.order;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.gate.controller.business.AbstractController;
import ru.kuznetsov.shop.gate.util.RequestParamUtils;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.contract.order.BucketItemContract;
import ru.kuznetsov.shop.represent.contract.order.OrderContract;
import ru.kuznetsov.shop.represent.dto.order.BucketItemDto;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.GET;

@RestController
@RequestMapping("/order/bucket")
public class BucketItemController extends AbstractController<BucketItemDto, BucketItemContract> {

    private final OrderContract orderContract;

    public BucketItemController(BucketItemContract contractService, AuthContract authService, PermissionsConfig permissionsConfig, OrderContract orderContract) {
        super(contractService, authService, permissionsConfig);
        this.orderContract = orderContract;
    }

    @Override
    public ResponseEntity<Collection<BucketItemDto>> getAllForUser(String token, @Nullable Map<String, String> reqParam) {
        if (hasAccess(token, GET)) {
            UUID userId = getUserIdFromToken(token);
            Long orderId = RequestParamUtils.getParamLongValue(reqParam, "orderId");

            if (orderId != null) {
                return orderContract.getById(orderId).getCustomerId().equals(userId.toString()) ?
                        ResponseEntity.ok(contractService.getAllByOrderId(orderId)) :
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                return ResponseEntity.ok(contractService.getAllByCustomerId(userId));
            }
        } else return ResponseEntity.status(401).build();
    }
}
