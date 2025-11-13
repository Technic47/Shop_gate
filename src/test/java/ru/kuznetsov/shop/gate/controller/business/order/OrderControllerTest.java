package ru.kuznetsov.shop.gate.controller.business.order;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.LinkedMultiValueMap;
import ru.kuznetsov.shop.gate.controller.business.AbstractControllerTest;
import ru.kuznetsov.shop.represent.contract.order.OrderContract;
import ru.kuznetsov.shop.represent.dto.order.OrderDto;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends AbstractControllerTest<OrderDto, OrderContract> {

    @MockitoBean
    private OrderContract contract;

    @Test
    void getAllForUser_return_200_with_user_no_arguments() throws Exception {
        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), new LinkedMultiValueMap<>(), null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());

        verify(contract).getAllByCustomerId(any(UUID.class));
    }

    @Test
    void getAllForUser_return_200_with_admin() throws Exception {
        doReturn(List.of(getMockDto()))
                .when(contract)
                .getAllByCustomerId(any(UUID.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllForUser_return_401_with_user_invalid_token() throws Exception {
        doReturn(false)
                .when(authService)
                .isTokenValid(any(String.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void getAllForUser_return_400_with_user_no_token() throws Exception {
        sendRequest(HttpMethod.GET, getApiPath())
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Override
    protected OrderContract getContract() {
        return contract;
    }

    @Override
    protected String getApiPath() {
        return "/order";
    }

    @Override
    protected OrderDto getMockDto() {
        return new OrderDto();
    }
}