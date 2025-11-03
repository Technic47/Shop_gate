package ru.kuznetsov.shop.gate.controller.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.gate.controller.AbstractIntegrationTest;
import ru.kuznetsov.shop.represent.contract.business.AbstractContract;
import ru.kuznetsov.shop.represent.dto.AbstractDto;

import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

abstract class AbstractControllerTest<E extends AbstractDto, S extends AbstractContract<E>> extends AbstractIntegrationTest {

    @BeforeEach
    void setup() {
        S contract = getContract();

        doReturn(true)
                .when(authService)
                .isTokenValid(any(String.class));

        doReturn(List.of("USER"))
                .when(authService)
                .getUserRoles("Bearer " + TEST_USER_LOGIN + ":" + TEST_USER_PASSWORD);

        doReturn(List.of("ADMIN"))
                .when(authService)
                .getUserRoles("Bearer " + TEST_ADMIN_LOGIN + ":" + TEST_ADMIN_PASSWORD);

        doReturn(getMockDto())
                .when(contract)
                .getById(any(Long.class));

        doReturn(List.of(getMockDto()))
                .when(contract)
                .getAll();

        doNothing()
                .when(contract)
                .delete(any(Long.class));
    }

    protected abstract S getContract();

    protected abstract String getApiPath();

    protected abstract E getMockDto();

    @Test
    void getById_return_200_with_User() throws Exception {
        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/1", TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getById_return_200_with_Admin() throws Exception {
        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/1", TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getById_return_400_with_no_token() throws Exception {
        sendRequest(HttpMethod.GET, getApiPath() + "/1")
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void getAll_return_200_with_User() throws Exception {
        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/all", TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAll_return_200_with_Admin() throws Exception {
        sendRequestWithAuthToken(HttpMethod.GET, getApiPath() + "/all", TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAll_return_400_with_no_token() throws Exception {
        sendRequest(HttpMethod.GET, getApiPath() + "/all")
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void add_return_401_with_User() throws Exception {
        E mockDto = getMockDto();

        sendRequestWithAuthToken(HttpMethod.POST, getApiPath() + "/add", mockDto, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void add_return_200_with_Admin() throws Exception {
        E mockDto = getMockDto();

        doReturn(mockDto)
                .when(getContract())
                .create((E) any(AbstractDto.class));

        sendRequestWithAuthToken(HttpMethod.POST, getApiPath() + "/add", mockDto, TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void add_return_400_with_no_token() throws Exception {
        E mockDto = getMockDto();

        sendRequest(HttpMethod.POST, getApiPath() + "/add", mockDto)
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void addBatch_return_401_with_User() throws Exception {
        E mockDto = getMockDto();

        sendRequestWithAuthToken(HttpMethod.POST, getApiPath() + "/add/batch", List.of(mockDto), TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void addBatch_return_200_with_Admin() throws Exception {
        E mockDto = getMockDto();

        doReturn(List.of(mockDto))
                .when(getContract())
                .createBatch(any(Collection.class));

        sendRequestWithAuthToken(HttpMethod.POST, getApiPath() + "/add/batch", List.of(mockDto), TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addBatch_return_400_with_no_token() throws Exception {
        E mockDto = getMockDto();

        sendRequest(HttpMethod.POST, getApiPath() + "/add/batch", List.of(mockDto))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void update_return_401_with_User() throws Exception {
        E mockDto = getMockDto();

        sendRequestWithAuthToken(HttpMethod.PUT, getApiPath() + "/1", mockDto, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

//    @Test
//    void update_return_200_with_Admin() throws Exception {
//        E mockDto = getMockDto();
//
//        sendRequestWithAuthToken(HttpMethod.PUT, getApiPath() + "/1", mockDto, TEST_USER_LOGIN, TEST_USER_PASSWORD)
//                .andDo(print())
//                .andExpect(status().is(401));
//    }

    @Test
    void update_return_400_with_no_token() throws Exception {
        E mockDto = getMockDto();

        sendRequest(HttpMethod.PUT, getApiPath() + "/1", mockDto)
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void delete_return_401_with_User() throws Exception {
        sendRequestWithAuthToken(HttpMethod.DELETE, getApiPath() + "/1", null, TEST_USER_LOGIN, TEST_USER_PASSWORD)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void delete_return_200_with_Admin() throws Exception {
        sendRequestWithAuthToken(HttpMethod.DELETE, getApiPath() + "/1", null, TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void delete_return_400_with_no_token() throws Exception {
        sendRequest(HttpMethod.DELETE, getApiPath() + "/1")
                .andDo(print())
                .andExpect(status().is(400));
    }
}