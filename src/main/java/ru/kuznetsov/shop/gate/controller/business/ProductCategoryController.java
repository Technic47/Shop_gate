package ru.kuznetsov.shop.gate.controller.business;

import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.contract.business.ProductCategoryContract;
import ru.kuznetsov.shop.represent.dto.ProductCategoryDto;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController extends AbstractController<ProductCategoryDto, ProductCategoryContract> {

    public ProductCategoryController(ProductCategoryContract contractService, AuthContract authService, PermissionsConfig permissionsConfig) {
        super(contractService, authService, permissionsConfig);
    }

    @Override
    public ResponseEntity<Collection<ProductCategoryDto>> getAllForUser(String token, @Nullable Map<String, String> reqParam) {
        return getAllBulk(token);
    }
}
