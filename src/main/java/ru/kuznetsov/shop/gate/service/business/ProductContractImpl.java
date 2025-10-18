package ru.kuznetsov.shop.gate.service.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.represent.contract.business.ProductContract;
import ru.kuznetsov.shop.represent.dto.ProductDto;

import java.util.Collection;

import static ru.kuznetsov.shop.gate.common.ConstValues.PRODUCT_MODULE;

@Service
public class ProductContractImpl extends AbstractContractImpl<ProductDto> implements ProductContract {

    protected ProductContractImpl(@Qualifier("product") WebClient webClient) {
        super(webClient);
    }

    @Override
    protected String getModuleName() {
        return PRODUCT_MODULE;
    }

    @Override
    public Collection<Boolean> createBatch(Collection<ProductDto> productDtoList) {
        return sendPostRequest(getModuleName() + "/batch", null, productDtoList, Boolean.class);
    }
}
