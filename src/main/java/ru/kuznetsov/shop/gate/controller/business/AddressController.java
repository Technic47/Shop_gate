package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.contract.business.AddressContract;
import ru.kuznetsov.shop.represent.dto.AddressDto;

@RestController
@RequestMapping("/address")
public class AddressController extends AbstractController<AddressDto, AddressContract> {

    public AddressController(AddressContract contractService, AuthContract authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }
}
