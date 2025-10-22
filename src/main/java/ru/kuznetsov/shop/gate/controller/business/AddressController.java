package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.represent.contract.business.AddressContract;
import ru.kuznetsov.shop.represent.dto.AddressDto;

import java.util.Collection;

@RestController
@RequestMapping("/address")
public class AddressController extends AbstractController<AddressDto, AddressContract> {

    protected AddressController(AddressContract service) {
        super(service);
    }

    @Override
    public ResponseEntity<AddressDto> add(AddressDto entity) {
        return ResponseEntity.ok(contract.create(entity));
    }

    @Override
    public ResponseEntity<Collection<AddressDto>> addBatch(Collection<AddressDto> addressDtoCollection) {
        return ResponseEntity.ok(contract.createBatch(addressDtoCollection));
    }

    @Override
    public ResponseEntity<AddressDto> update(Long id, AddressDto entity) {
        return new ResponseEntity("Method not implemented yet", HttpStatus.NOT_IMPLEMENTED);
    }
}
