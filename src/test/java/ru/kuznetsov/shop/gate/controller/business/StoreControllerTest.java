package ru.kuznetsov.shop.gate.controller.business;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.kuznetsov.shop.represent.contract.business.StoreContract;
import ru.kuznetsov.shop.represent.dto.StoreDto;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StoreControllerTest extends AbstractControllerTest<StoreDto, StoreContract> {

    @MockitoBean
    private StoreContract contract;

    @Test
    void getAllStores_return_200_with_user_and_arguments() throws Exception {
        Long id = 1L;
        String name = "name";
        Long addressId = 2L;

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("id", id.toString());
        requestParams.add("name", name);
        requestParams.add("addressId", addressId.toString());

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), requestParams, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());

        verify(contract).getAll(eq(id), eq(name), eq(addressId), any(UUID.class));
    }

    @Test
    void getAllStores_return_200_with_user_no_arguments() throws Exception {
        doReturn(List.of(getMockDto()))
                .when(contract)
                .getAll(any(Long.class), any(String.class), any(Long.class), any(UUID.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());

        verify(contract).getAll(eq(null), eq(null), eq(null), any(UUID.class));
    }

    @Test
    void getAllStores_return_200_with_admin() throws Exception {
        doReturn(List.of(getMockDto()))
                .when(contract)
                .getAll(any(Long.class), any(String.class), any(Long.class), any(UUID.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllStores_return_401_with_user_invalid_token() throws Exception {
        doReturn(false)
                .when(authService)
                .isTokenValid(any(String.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath(), TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void getAllStores_return_400_with_user_no_token() throws Exception {
        sendRequest(HttpMethod.GET, getApiPath())
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Override
    protected StoreContract getContract() {
        return contract;
    }

    @Override
    protected String getApiPath() {
        return "/store";
    }

    @Override
    protected StoreDto getMockDto() {
        StoreDto dto = new StoreDto();
        dto.setName("test");
        dto.setAddressId(1L);
        dto.setAddress("test");

        return dto;
    }
}