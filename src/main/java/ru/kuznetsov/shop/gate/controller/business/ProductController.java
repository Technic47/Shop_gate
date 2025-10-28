package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.represent.contract.business.ProductContract;
import ru.kuznetsov.shop.represent.dto.ProductDto;

@RestController
@RequestMapping("/product")
public class ProductController extends AbstractController<ProductDto, ProductContract> {

    protected ProductController(ProductContract service) {
        super(service);
    }
}
