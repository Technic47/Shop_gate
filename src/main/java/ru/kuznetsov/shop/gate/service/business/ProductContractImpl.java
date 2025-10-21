package ru.kuznetsov.shop.gate.service.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.gate.service.OperationService;
import ru.kuznetsov.shop.represent.contract.business.ProductContract;
import ru.kuznetsov.shop.represent.dto.ProductDto;

import java.util.Collection;

import static ru.kuznetsov.shop.gate.common.ConstValues.PRODUCT_MODULE;

@Service
public class ProductContractImpl extends AbstractContractImpl<ProductDto> implements ProductContract {

    private final OperationService operationService;

    protected ProductContractImpl(@Qualifier("product") WebClient webClient, OperationService operationService1) {
        super(webClient);
        this.operationService = operationService1;
    }

    @Override
    protected String getModuleName() {
        return PRODUCT_MODULE;
    }

    @Override
    public ProductDto create(ProductDto entity) {
        String operationId = sendPostRequest(getModuleName(), null, entity, String.class).get(0);
        Long entityId = operationService.getEntityIdsByOperationId(operationId).get(0);

        return getById(entityId);
    }

    @Override
    public Collection<ProductDto> createBatch(Collection<ProductDto> entities) {
        String operationId = sendPostRequest(getModuleName() + "/batch", null, entities, String.class).get(0);
        return operationService.getEntityIdsByOperationId(operationId)
                .stream()
                .map(this::getById)
                .toList();
    }
}
