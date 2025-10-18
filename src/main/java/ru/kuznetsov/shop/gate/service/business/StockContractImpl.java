package ru.kuznetsov.shop.gate.service.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.represent.contract.business.StockContract;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collection;

import static ru.kuznetsov.shop.gate.common.ConstValues.STOCK_MODULE;

@Service
public class StockContractImpl extends AbstractContractImpl<StockDto> implements StockContract {

    protected StockContractImpl(@Qualifier("stock") WebClient webClient) {
        super(webClient);
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
    public Collection<Boolean> createBatch(Collection<StockDto> stockDtoList) {
        return sendPostRequest(getModuleName() + "/batch", null, stockDtoList, Boolean.class);
    }
}
