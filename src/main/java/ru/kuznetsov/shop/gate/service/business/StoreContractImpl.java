package ru.kuznetsov.shop.gate.service.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.represent.contract.business.StoreContract;
import ru.kuznetsov.shop.represent.dto.StockDto;
import ru.kuznetsov.shop.represent.dto.StoreDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.gate.common.ConstValues.STORE_MODULE;

@Service
public class StoreContractImpl extends AbstractContractImpl<StoreDto> implements StoreContract {
    protected StoreContractImpl(@Qualifier("store") WebClient webClient) {
        super(webClient);
    }

    @Override
    protected String getModuleName() {
        return STORE_MODULE;
    }

    @Override
    public List<StoreDto> getAll(Long id, String name, Long addressId) {
        Map<String, Object> params = new HashMap<>();
        if (id != null) params.put("id", id);
        if (name != null && !name.isEmpty()) params.put("name", name);
        if (addressId != null) params.put("addressId", addressId);

        return sendGetRequest(getModuleName(), params, null, StoreDto.class);
    }

    @Override
    public List<StockDto> getAllStockByStoreId(Long storeId) {
        return sendGetRequest(getModuleName() + "/" + storeId + "/stock", null, null, StockDto.class);
    }
}
