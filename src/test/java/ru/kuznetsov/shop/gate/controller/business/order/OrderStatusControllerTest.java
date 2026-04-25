package ru.kuznetsov.shop.gate.controller.business.order;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.kuznetsov.shop.gate.controller.business.AbstractControllerTest;
import ru.kuznetsov.shop.represent.contract.order.OrderStatusContract;
import ru.kuznetsov.shop.represent.dto.order.OrderStatusDto;
import ru.kuznetsov.shop.represent.enums.OrderStatusType;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
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

    @Test
    void getByStatus_return_200_with_admin_status_only() throws Exception {
        // ORDER_GET requires SELLER or ADMIN
        doReturn(List.of(getMockDto()))
                .when(contract)
                .getAllByStatus(any(OrderStatusType.class), isNull(), isNull(), isNull());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("status", OrderStatusType.CREATED.name());

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/status", params, null, TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());

        verify(contract).getAllByStatus(OrderStatusType.CREATED, null, null, null);
    }

    @Test
    void getByStatus_return_200_with_admin_all_params() throws Exception {
        doReturn(List.of(getMockDto()))
                .when(contract)
                .getAllByStatus(any(OrderStatusType.class), any(), any(), any());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("status", OrderStatusType.SHIPPED.name());
        params.add("dateTime", "2026-01-01T00:00:00");
        params.add("direction", "ASC");
        params.add("limit", "10");

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/status", params, null, TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getByStatus_return_401_with_user() throws Exception {
        // ORDER_GET requires SELLER or ADMIN — USER is denied
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("status", OrderStatusType.CREATED.name());

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/status", params, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void getByStatus_return_400_no_token() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("status", OrderStatusType.CREATED.name());

        sendRequest(HttpMethod.GET, getApiPath() + "/status", params)
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