package ru.kuznetsov.shop.gate.controller.business;

import org.junit.jupiter.api.Test;
import ru.kuznetsov.shop.represent.contract.business.StoreContract;
import ru.kuznetsov.shop.represent.dto.StoreDto;

class StoreControllerTest extends AbstractControllerTest<StoreDto, StoreContract> {

    @Test
    void getAllStores() {
    }

    @Test
    void getAllStockByStoreId() {
    }

    @Override
    protected String getApiPath() {
        return "/store";
    }

    @Override
    protected StoreDto getMockDto() {
        StoreDto dto = new StoreDto();
        dto.setName("test");
        dto.setAddressId(1L);
        dto.setAddress("test");

        return dto;
    }
}