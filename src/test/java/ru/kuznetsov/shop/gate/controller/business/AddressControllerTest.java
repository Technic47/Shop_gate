package ru.kuznetsov.shop.gate.controller.business;

import ru.kuznetsov.shop.represent.contract.business.AddressContract;
import ru.kuznetsov.shop.represent.dto.AddressDto;

class AddressControllerTest extends AbstractControllerTest<AddressDto, AddressContract> {

    @Override
    protected String getApiPath() {
        return "/address";
    }

    protected AddressDto getMockDto() {
        AddressDto dto = new AddressDto();
        dto.setCity("Test");
        dto.setStreet("Test");
        dto.setHouse("123");

        return dto;
    }
}