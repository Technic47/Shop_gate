package ru.kuznetsov.shop.gate.controller.business.order;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.kuznetsov.shop.gate.controller.business.AbstractControllerTest;
import ru.kuznetsov.shop.represent.contract.order.BucketItemContract;
import ru.kuznetsov.shop.represent.contract.order.OrderContract;
import ru.kuznetsov.shop.represent.dto.order.BucketItemDto;
import ru.kuznetsov.shop.represent.dto.order.OrderDto;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BucketItemControllerTest extends AbstractControllerTest<BucketItemDto, BucketItemContract> {

    @MockitoBean
    private BucketItemContract contract;

    @MockitoBean
    private OrderContract orderContract;

    @Test
    void getAllForUser_return_200_with_user_with_arguments_same_customer() throws Exception {
        Long orderId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderId", orderId.toString());

        doReturn(getMockOrderDto())
                .when(orderContract)
                .getById(any(Long.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), requestParams, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());

        verify(contract).getAllByOrderId(orderId);
    }

    @Test
    void getAllForUser_return_404_with_user_with_arguments_not_same_customer() throws Exception {
        long orderId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderId", Long.toString(orderId));

        OrderDto mockOrderDto = getMockOrderDto();
        mockOrderDto.setCustomerId("123");

        doReturn(mockOrderDto)
                .when(orderContract)
                .getById(any(Long.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), requestParams, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(contract);
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
        long orderId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderId", Long.toString(orderId));

        doReturn(List.of(getMockDto()))
                .when(contract)
                .getAllByOrderId(any(Long.class));

        doReturn(getMockOrderDto())
                .when(orderContract)
                .getById(any(Long.class));

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

    @Override
    protected BucketItemContract getContract() {
        return contract;
    }

    @Override
    protected String getApiPath() {
        return "/order/bucket";
    }

    @Override
    protected BucketItemDto getMockDto() {
        return new BucketItemDto();
    }

    private OrderDto getMockOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(MOCK_USER_ID.toString());

        return orderDto;
    }
}