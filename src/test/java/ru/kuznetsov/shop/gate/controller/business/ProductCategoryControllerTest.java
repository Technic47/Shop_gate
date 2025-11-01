package ru.kuznetsov.shop.gate.controller.business;

import ru.kuznetsov.shop.represent.contract.business.ProductCategoryContract;
import ru.kuznetsov.shop.represent.dto.ProductCategoryDto;

class ProductCategoryControllerTest extends AbstractControllerTest<ProductCategoryDto, ProductCategoryContract> {

    @Override
    protected String getApiPath() {
        return "/product-category";
    }

    @Override
    protected ProductCategoryDto getMockDto() {
        ProductCategoryDto dto = new ProductCategoryDto();
        dto.setName("test");
        dto.setDescription("test");

        return dto;
    }
}