package ru.kuznetsov.shop.gate.controller.business;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.kuznetsov.shop.represent.contract.business.ProductContract;
import ru.kuznetsov.shop.represent.dto.ProductCardDto;
import ru.kuznetsov.shop.represent.dto.ProductDto;
import ru.kuznetsov.shop.represent.dto.util.ProductCardPage;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends AbstractControllerTest<ProductDto, ProductContract> {

    @MockitoBean
    private ProductContract contract;

    @Test
    void getAllCard_return_200_with_user() throws Exception {
        doReturn(List.of(new ProductCardDto()))
                .when(contract)
                .getProductCardsByOwnerIdAOrCategoryId(any(), any());

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/card", TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllCard_return_200_with_category_filter() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("categoryId", "1");

        doReturn(List.of(new ProductCardDto()))
                .when(contract)
                .getProductCardsByOwnerIdAOrCategoryId(any(), any());

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/card", params, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllCard_return_401_with_invalid_token() throws Exception {
        doReturn(false)
                .when(authService)
                .isTokenValid(any(String.class));

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/card", TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void getAllCard_return_400_no_token() throws Exception {
        sendRequest(HttpMethod.GET, getApiPath() + "/card")
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void getAllCardPageable_return_200_with_user() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pageNumber", "0");
        params.add("pageSize", "10");

        doReturn(new ProductCardPage())
                .when(contract)
                .getProductCardDtoPageable(anyInt(), anyInt(), any(), any());

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/card/page", params, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllCardPageable_return_200_with_category_filter() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pageNumber", "0");
        params.add("pageSize", "10");
        params.add("categoryId", "1");

        doReturn(new ProductCardPage())
                .when(contract)
                .getProductCardDtoByCategoryOrOwnerIdPageable(any(), any(), anyInt(), anyInt(), any(), any());

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/card/page", params, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllCardPageable_return_400_invalid_page_number() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pageNumber", "-1");
        params.add("pageSize", "10");

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/card/page", params, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void getAllCardPageable_return_401_with_invalid_token() throws Exception {
        doReturn(false)
                .when(authService)
                .isTokenValid(any(String.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pageNumber", "0");
        params.add("pageSize", "10");

        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/card/page", params, null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void getAllCardPageable_return_400_no_token() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pageNumber", "0");
        params.add("pageSize", "10");

        sendRequest(HttpMethod.GET, getApiPath() + "/card/page", params)
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Override
    protected ProductContract getContract() {
        return contract;
    }

    @Override
    protected String getApiPath() {
        return "/product";
    }

    @Override
    protected ProductDto getMockDto() {
        ProductDto dto = new ProductDto();
        dto.setName("Test");
        dto.setDescription("Test");
        dto.setPrice(123);

        return dto;
    }
}
