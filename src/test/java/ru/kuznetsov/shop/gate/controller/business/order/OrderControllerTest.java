package ru.kuznetsov.shop.gate.controller.business.order;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.kuznetsov.shop.gate.controller.business.AbstractControllerTest;
import ru.kuznetsov.shop.represent.contract.order.OrderContract;
import ru.kuznetsov.shop.represent.dto.order.OrderDto;
import ru.kuznetsov.shop.represent.dto.order.OrderThinDto;
import ru.kuznetsov.shop.represent.enums.OrderStatusType;

import java.util.Collection;
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

    // ORDER_SAVE allows USER — overrides parent test that expects 401 for the default SAVE permission
    @Override
    @Test
    protected void add_return_401_with_User() throws Exception {
        OrderDto mockDto = getMockDto();
        doReturn(mockDto).when(contract).create(any(OrderDto.class));

        sendRequestWithAuthToken(HttpMethod.POST, getApiPath(), mockDto, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    // ORDER_SAVE allows USER — overrides parent test that expects 401 for the default SAVE permission
    @Override
    @Test
    protected void addBatch_return_401_with_User() throws Exception {
        OrderDto mockDto = getMockDto();
        doReturn(List.of(mockDto)).when(contract).createBatch(any(Collection.class));

        sendRequestWithAuthToken(HttpMethod.POST, getApiPath() + "/batch", List.of(mockDto), TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    // ORDER_UPDATE allows USER — overrides parent test that expects 401 for the default UPDATE permission
    @Override
    @Test
    protected void update_return_401_with_User() throws Exception {
        OrderDto mockDto = getMockDto();
        doReturn(mockDto).when(contract).update(any(OrderDto.class));

        sendRequestWithAuthToken(HttpMethod.PUT, getApiPath(), mockDto, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

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

    @Test
    void getAllByStatusAndOptionalParams_return_200_with_admin() throws Exception {
        doReturn(List.of(new OrderThinDto()))
                .when(contract)
                .getAllByStatusAndOptionalParams(any(), any(), any(), any(), any());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("hasStatus", OrderStatusType.CREATED.name());
        params.add("hasNotStatus", OrderStatusType.CANCELED.name());

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/hasStatus", params, null, TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllByStatusAndOptionalParams_return_200_with_optional_params() throws Exception {
        doReturn(List.of(new OrderThinDto()))
                .when(contract)
                .getAllByStatusAndOptionalParams(any(), any(), any(), any(), any());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("hasStatus", OrderStatusType.READY.name());
        params.add("hasNotStatus", OrderStatusType.DELIVERED.name());
        params.add("customerId", UUID.randomUUID().toString());
        params.add("dateAfter", "2026-01-01T00:00:00");
        params.add("dateBefore", "2026-12-31T23:59:59");

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/hasStatus", params, null, TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllByStatusAndOptionalParams_return_204_empty_result() throws Exception {
        doReturn(List.of())
                .when(contract)
                .getAllByStatusAndOptionalParams(any(), any(), any(), any(), any());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("hasStatus", OrderStatusType.CREATED.name());
        params.add("hasNotStatus", OrderStatusType.CANCELED.name());

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/hasStatus", params, null, TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    void getAllByStatusAndOptionalParams_return_401_with_user() throws Exception {
        // ORDER_GET requires SELLER or ADMIN — USER is denied
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("hasStatus", OrderStatusType.CREATED.name());
        params.add("hasNotStatus", OrderStatusType.CANCELED.name());

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/hasStatus", params, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void getAllByStatusAndOptionalParams_return_400_no_token() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("hasStatus", OrderStatusType.CREATED.name());
        params.add("hasNotStatus", OrderStatusType.CANCELED.name());

        sendRequest(HttpMethod.GET, getApiPath() + "/hasStatus", params)
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
