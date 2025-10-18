package ru.kuznetsov.shop.gate.service.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.represent.contract.business.ProductCategoryContract;
import ru.kuznetsov.shop.represent.dto.ProductCategoryDto;

import static ru.kuznetsov.shop.gate.common.ConstValues.PRODUCT_CATEGORY_MODULE;

@Service
public class ProductCategoryContractImpl extends AbstractContractImpl<ProductCategoryDto> implements ProductCategoryContract {
    protected ProductCategoryContractImpl(@Qualifier("product-category") WebClient webClient) {
        super(webClient);
    }

    @Override
    protected String getModuleName() {
        return PRODUCT_CATEGORY_MODULE;
    }
}
