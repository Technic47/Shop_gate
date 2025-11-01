package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.gate.service.AuthService;
import ru.kuznetsov.shop.represent.contract.business.StoreContract;
import ru.kuznetsov.shop.represent.dto.StockDto;
import ru.kuznetsov.shop.represent.dto.StoreDto;

import java.util.Collection;
import java.util.List;

import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.GET;

@RestController
@RequestMapping("/store")
public class StoreController extends AbstractController<StoreDto, StoreContract> {

    public StoreController(StoreContract contractService, AuthService authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }

    @GetMapping()
    public ResponseEntity<List<StoreDto>> getAllStores(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long addressId
    ) {
        if (hasAccess(token, GET)) {
            return ResponseEntity.ok(contractService.getAll(id, name, addressId));
        } else return ResponseEntity.status(401).build();
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Collection<StockDto>> getAllStockByStoreId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable long id) {
        if (hasAccess(token, GET)) {
            return ResponseEntity.ok(contractService.getAllStockByStoreId(id));
        } else return ResponseEntity.status(401).build();
    }
}
