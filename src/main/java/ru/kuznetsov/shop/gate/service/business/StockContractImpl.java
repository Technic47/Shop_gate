package ru.kuznetsov.shop.gate.service.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.gate.service.OperationService;
import ru.kuznetsov.shop.represent.contract.business.StockContract;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collection;

import static ru.kuznetsov.shop.gate.common.ConstValues.STOCK_MODULE;

@Service
public class StockContractImpl extends AbstractContractImpl<StockDto> implements StockContract {

    private final OperationService operationService;

    protected StockContractImpl(@Qualifier("stock") WebClient webClient, OperationService operationService) {
        super(webClient);
        this.operationService = operationService;
    }

    @Override
    protected String getModuleName() {
        return STOCK_MODULE;
    }

    @Override
    public Collection<StockDto> getAllByStoreId(Long storeId) {
        return sendGetRequest(getModuleName() + "/store", null, null, StockDto.class);
    }

    @Override
    public Collection<StockDto> getAllByProductId(Long productId) {
        return sendGetRequest(getModuleName() + "/product", null, null, StockDto.class);
    }


    @Override
    public StockDto create(StockDto entity) {
        String operationId = sendPostRequest(getModuleName(), null, entity, String.class).get(0);
        Long entityId = operationService.getEntityIdsByOperationId(operationId).get(0);

        return getById(entityId);
    }

    @Override
    public Collection<StockDto> createBatch(Collection<StockDto> entities) {
        String operationId = sendPostRequest(getModuleName() + "/batch", null, entities, String.class).get(0);
        return operationService.getEntityIdsByOperationId(operationId)
                .stream()
                .map(this::getById)
                .toList();
    }
}
