package ru.kuznetsov.shop.gate.controller.business.order;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.kuznetsov.shop.gate.controller.business.AbstractControllerTest;
import ru.kuznetsov.shop.represent.contract.order.OrderStatusContract;
import ru.kuznetsov.shop.represent.dto.order.OrderStatusDto;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderStatusControllerTest extends AbstractControllerTest<OrderStatusDto, OrderStatusContract> {

    @MockitoBean
    private OrderStatusContract contract;

    @Test
    void getAllForUser_return_200_with_user_with_arguments() throws Exception {
        Long orderId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderId", orderId.toString());

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), requestParams, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());

        verify(contract).getAllByOrderId(orderId);
    }

    @Test
    void getAllForUser_return_200_with_user_no_arguments() throws Exception {
        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), new LinkedMultiValueMap<>(), null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllForUser_return_200_with_admin() throws Exception {
        long orderId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderId", Long.toString(orderId));

        doReturn(List.of(getMockDto()))
                .when(contract)
                .getAllByOrderId(any(Long.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), requestParams, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
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

    @Test
    void getLast_return_200_with_user_with_arguments_has_result() throws Exception {
        Long orderId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderId", orderId.toString());

        doReturn(Optional.of(getMockDto()))
                .when(contract)
                .getLastByOrderId(any(Long.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/last", requestParams, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());

        verify(contract).getLastByOrderId(orderId);
    }

    @Test
    void getLast_return_200_with_user_with_arguments_empty_result() throws Exception {
        Long orderId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderId", orderId.toString());

        doReturn(Optional.empty())
                .when(contract)
                .getLastByOrderId(any(Long.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/last", requestParams, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(204));

        verify(contract).getLastByOrderId(orderId);
    }

    @Test
    void getLast_return_200_with_user_no_arguments() throws Exception {
        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/last", new LinkedMultiValueMap<>(), null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLast_return_401_with_user_invalid_token() throws Exception {
        long orderId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderId", Long.toString(orderId));

        doReturn(false)
                .when(authService)
                .isTokenValid(any(String.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/last", requestParams, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void getLast_return_400_with_user_no_token() throws Exception {
        sendRequest(HttpMethod.GET, getApiPath() + "/last")
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Override
    protected OrderStatusContract getContract() {
        return contract;
    }

    @Override
    protected String getApiPath() {
        return "/order/status";
    }

    @Override
    protected OrderStatusDto getMockDto() {
        return new OrderStatusDto();
    }
}