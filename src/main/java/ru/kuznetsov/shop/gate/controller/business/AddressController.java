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
import ru.kuznetsov.shop.represent.contract.business.AddressContract;
import ru.kuznetsov.shop.represent.dto.AddressDto;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController extends AbstractController<AddressDto, AddressContract> {

    public AddressController(AddressContract contractService, AuthContract authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }

    @Override
    public ResponseEntity<Collection<AddressDto>> getAllForUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Nullable @RequestParam(required = false) Map<String, String> reqParam
    ) {
        return getAllBulk(token);
    }
}
