package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.represent.contract.business.AddressContract;
import ru.kuznetsov.shop.represent.dto.AddressDto;

@RestController
@RequestMapping("/address")
public class AddressController extends AbstractController<AddressDto, AddressContract> {

    protected AddressController(AddressContract service) {
        super(service);
    }
}
