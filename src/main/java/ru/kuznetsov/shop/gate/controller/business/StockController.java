package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.gate.service.AuthService;
import ru.kuznetsov.shop.represent.contract.business.StockContract;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collection;

import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.GET;

@RestController
@RequestMapping("/stock")
public class StockController extends AbstractController<StockDto, StockContract> {

    public StockController(StockContract contractService, AuthService authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }

    @GetMapping("/{id}/store")
    public ResponseEntity<Collection<StockDto>> getAllByStoreId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable long id) {
        if (hasAccess(token, GET)) {
            return ResponseEntity.ok(contractService.getAllByStoreId(id));
        } else return ResponseEntity.status(401).build();
    }

    @GetMapping("/{id}/product")
    public ResponseEntity<Collection<StockDto>> getAllByProductId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable long id) {
        if (hasAccess(token, GET)) {
            return ResponseEntity.ok(contractService.getAllByProductId(id));
        } else return ResponseEntity.status(401).build();
    }
}
