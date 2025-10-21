package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.gate.service.business.ProductContractImpl;
import ru.kuznetsov.shop.represent.dto.ProductDto;

import java.util.Collection;

@RestController
@RequestMapping("/product")
public class ProductController extends AbstractController<ProductDto, ProductContractImpl> {

    protected ProductController(ProductContractImpl service) {
        super(service);
    }

    @Override
    public ResponseEntity<ProductDto> add(ProductDto entity) {
        return null;
    }

    @Override
    public ResponseEntity<Collection<ProductDto>> addBatch(Collection<ProductDto> entity) {
        return null;
    }

    @Override
    public ResponseEntity<ProductDto> update(Long id, ProductDto entity) {
        return new ResponseEntity("Method not implemented yet", HttpStatus.NOT_IMPLEMENTED);
    }
}
