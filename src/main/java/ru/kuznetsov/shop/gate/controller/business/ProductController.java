package ru.kuznetsov.shop.gate.controller.business;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.gate.config.PermissionsConfig;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.contract.business.ProductContract;
import ru.kuznetsov.shop.represent.dto.ProductCardDto;
import ru.kuznetsov.shop.represent.dto.ProductDto;
import ru.kuznetsov.shop.represent.dto.util.ProductCardPage;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.GET_ALL;
import static ru.kuznetsov.shop.gate.enums.UserPermissionEnum.PRODUCT_GET_ALL_CARDS;
import static ru.kuznetsov.shop.gate.util.RequestParamUtils.*;

@RestController
@RequestMapping("/product")
public class ProductController extends AbstractController<ProductDto, ProductContract> {

    private final ProductContract productContract;

    public ProductController(ProductContract contractService, AuthContract authService, PermissionsConfig permissionsConfig, ProductContract productContract) {
        super(contractService, authService, permissionsConfig);
        this.productContract = productContract;
    }

    public ResponseEntity<Collection<ProductDto>> getAllForUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Nullable @RequestParam(required = false) Map<String, String> reqParam) {
        if (hasAccess(token, GET_ALL)) {
            UUID userId = getUserIdFromToken(token);
            UUID ownerId = getParamUUIDValue(reqParam, OWNER_ID_PARAMETER);
            Long categoryId = getParamLongValue(reqParam, CATEGORY_ID_PARAMETER);

            if (ownerId != null) {
                return ResponseEntity.ok(contractService.getAllByOwnerIdOrCategoryId(userId, categoryId));
            } else return ResponseEntity.ok(contractService.getAllByOwnerId(userId));
        } else return ResponseEntity.status(401).build();
    }

    @GetMapping("/card")
    public ResponseEntity<Collection<ProductCardDto>> getAllCard(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) Long categoryId
    ) {
        if (hasAccess(token, PRODUCT_GET_ALL_CARDS)) {
            return ResponseEntity.ok(productContract.getProductCardsByOwnerIdAOrCategoryId(
                    (ownerId == null || ownerId.isEmpty()) ? null : UUID.fromString(ownerId),
                    categoryId));
        } else return ResponseEntity.status(401).build();
    }

    @GetMapping("/card/page")
    public ResponseEntity<ProductCardPage> getAllCardPageable(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection
    ) {
        if (hasAccess(token, PRODUCT_GET_ALL_CARDS)) {
            if (pageNum < 0) {
                return ResponseEntity.status(400).build();
            } else {
                if (ownerId != null || categoryId != null) {
                    return ResponseEntity.ok(productContract.getProductCardDtoByCategoryOrOwnerIdPageable(
                            (ownerId == null || ownerId.isEmpty()) ? null : UUID.fromString(ownerId),
                            categoryId, pageNum, pageSize, sortBy, sortDirection
                    ));
                } else return ResponseEntity.ok(productContract.getProductCardDtoPageable(
                        pageNum, pageSize, sortBy, sortDirection
                ));
            }
        } else return ResponseEntity.status(401).build();
    }
}
