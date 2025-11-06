package ru.kuznetsov.shop.gate.controller.business;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.kuznetsov.shop.represent.contract.business.StockContract;
import ru.kuznetsov.shop.represent.dto.StockDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StockControllerTest extends AbstractControllerTest<StockDto, StockContract> {

    @MockitoBean
    private StockContract contract;

    @Test
    void getAllByStoreId_return_200_with_user() throws Exception {
        long storeId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("storeId", Long.toString(storeId));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), requestParams, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllByStoreId_return_200_with_admin() throws Exception {
        long storeId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("storeId", Long.toString(storeId));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), requestParams, null, TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllByStoreId_return_401_with_user_invalid_token() throws Exception {
        doReturn(false)
                .when(authService)
                .isTokenValid(any(String.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void getAllByStoreId_return_400_with_user_no_token() throws Exception {
        sendRequest(HttpMethod.GET, getApiPath())
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void getAllByProductId_return_200_with_user() throws Exception {
        long productId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("productId", Long.toString(productId));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), requestParams, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllByProductId_return_200_with_admin() throws Exception {
        long productId = 1L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("productId", Long.toString(productId));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), requestParams, null, TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllByProductId_return_401_with_user_invalid_token() throws Exception {
        doReturn(false)
                .when(authService)
                .isTokenValid(any(String.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void getAllByProductId_return_400_with_user_no_token() throws Exception {
        sendRequest(HttpMethod.GET, getApiPath())
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Override
    protected StockContract getContract() {
        return contract;
    }

    @Override
    protected String getApiPath() {
        return "/stock";
    }

    @Override
    protected StockDto getMockDto() {
        StockDto dto = new StockDto();
        dto.setAmount(1);
        dto.setProductId(1L);
        dto.setProductName("test");
        dto.setStore("test");
        dto.setStoreAddress("test");

        return dto;
    }
}