package ru.kuznetsov.shop.gate.controller.business;

import ru.kuznetsov.shop.represent.contract.business.ProductContract;
import ru.kuznetsov.shop.represent.dto.ProductDto;

class ProductControllerTest extends AbstractControllerTest<ProductDto, ProductContract> {

    @Override
    protected String getApiPath() {
        return "/product";
    }

    @Override
    protected ProductDto getMockDto() {
        ProductDto dto = new ProductDto();
        dto.setName("Test");
        dto.setDescription("Test");
        dto.setPrice(123);

        return dto;
    }
}