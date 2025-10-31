package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.gate.service.AuthService;
import ru.kuznetsov.shop.represent.contract.business.ProductCategoryContract;
import ru.kuznetsov.shop.represent.dto.ProductCategoryDto;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController extends AbstractController<ProductCategoryDto, ProductCategoryContract> {

    public ProductCategoryController(ProductCategoryContract contractService, AuthService authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }
}
