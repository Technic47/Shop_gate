package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.kuznetsov.shop.represent.contract.business.AddressContract;
import ru.kuznetsov.shop.represent.dto.AddressDto;

class AddressControllerTest extends AbstractControllerTest<AddressDto, AddressContract> {

    @MockitoBean
    private AddressContract contract;

    @Override
    protected AddressContract getContract() {
        return contract;
    }

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