package ru.kuznetsov.shop.gate.controller.business;

import org.junit.jupiter.api.Test;
import ru.kuznetsov.shop.represent.contract.business.StockContract;
import ru.kuznetsov.shop.represent.dto.StockDto;

class StockControllerTest extends AbstractControllerTest<StockDto, StockContract> {

    @Test
    void getAllByStoreId() {
    }

    @Test
    void getAllByProductId() {
    }

    @Override
    protected String getApiPath() {
        return "/stock";
    }

    @Override
    protected StockDto getMockDto() {
        StockDto dto = new StockDto();
        dto.setAmount(1);
        dto.setProductId(1L);
        dto.setProductName("test");
        dto.setStore("test");
        dto.setStoreAddress("test");

        return dto;
    }
}