package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.gate.service.business.ProductCategoryContractImpl;
import ru.kuznetsov.shop.represent.dto.ProductCategoryDto;

import java.util.Collection;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController extends AbstractController<ProductCategoryDto, ProductCategoryContractImpl> {

    protected ProductCategoryController(ProductCategoryContractImpl service) {
        super(service);
    }

    @Override
    public ResponseEntity<ProductCategoryDto> add(ProductCategoryDto entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @Override
    public ResponseEntity<Collection<ProductCategoryDto>> addBatch(Collection<ProductCategoryDto> productCategoryDtoCollection) {
        return ResponseEntity.ok(service.createBatch(productCategoryDtoCollection));
    }

    @Override
    public ResponseEntity<ProductCategoryDto> update(Long id, ProductCategoryDto entity) {
        return new ResponseEntity("Method not implemented yet", HttpStatus.NOT_IMPLEMENTED);
    }
}
