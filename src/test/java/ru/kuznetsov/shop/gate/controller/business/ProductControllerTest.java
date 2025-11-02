package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.kuznetsov.shop.represent.contract.business.ProductContract;
import ru.kuznetsov.shop.represent.dto.ProductDto;

class ProductControllerTest extends AbstractControllerTest<ProductDto, ProductContract> {

    @MockitoBean
    private ProductContract contract;

    @Override
    protected ProductContract getContract() {
        return contract;
    }

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