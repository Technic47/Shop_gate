package ru.kuznetsov.shop.gate.service.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.represent.contract.business.AddressContract;
import ru.kuznetsov.shop.represent.dto.AddressDto;

import static ru.kuznetsov.shop.gate.common.ConstValues.ADDRESS_MODULE;

@Service
public class AddressContractImpl extends AbstractContractImpl<AddressDto> implements AddressContract {

    protected AddressContractImpl(@Qualifier("address") WebClient webClient) {
        super(webClient);
    }

    @Override
    protected String getModuleName() {
        return ADDRESS_MODULE;
    }
}
