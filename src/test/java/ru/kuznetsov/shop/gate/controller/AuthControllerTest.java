package ru.kuznetsov.shop.gate.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import ru.kuznetsov.shop.represent.dto.auth.LoginPasswordDto;
import ru.kuznetsov.shop.represent.dto.auth.UserDto;
import ru.kuznetsov.shop.represent.dto.auth.UserRepresentationDto;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Order(1)
class AuthControllerTest extends AbstractIntegrationTest {

    protected final static String CHECK_TOKEN = "/check/token";
    protected final static String CHECK_ROLES = "/check/roles";
    protected final static String MOCK_TOKEN = "sghsfhfhrse5g";

    @Test
    void getToken_return_200_user() throws Exception {
        LoginPasswordDto request = new LoginPasswordDto(TEST_USER_LOGIN, TEST_USER_PASSWORD);

        sendRequest(HttpMethod.POST, AUTH_API_PATH, request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getToken_return_200_admin() throws Exception {
        LoginPasswordDto request = new LoginPasswordDto(TEST_ADMIN_LOGIN, TEST_ADMIN_PASSWORD);

        sendRequest(HttpMethod.POST, AUTH_API_PATH, request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getToken_return_401_wrong_login() throws Exception {
        doThrow(RuntimeException.class)
                .when(authService)
                .getToken(any(LoginPasswordDto.class));

        LoginPasswordDto request = new LoginPasswordDto(TEST_USER_LOGIN + "123", TEST_USER_PASSWORD);

        sendRequest(HttpMethod.POST, AUTH_API_PATH, request)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void checkToken_return_true() throws Exception {
        doReturn(true)
                .when(authService)
                .isTokenValid(any(String.class));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + MOCK_TOKEN);

        MvcResult mvcResult = sendRequest(HttpMethod.POST, AUTH_API_PATH + CHECK_TOKEN, new LinkedMultiValueMap<>(), headers, null)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(Boolean.parseBoolean(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    void checkToken_return_false() throws Exception {
        doReturn(false)
                .when(authService)
                .isTokenValid(any(String.class));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + MOCK_TOKEN);

        MvcResult mvcResult = sendRequest(HttpMethod.POST, AUTH_API_PATH + CHECK_TOKEN, new LinkedMultiValueMap<>(), headers, null)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertFalse(Boolean.parseBoolean(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    void checkToken_return_400_no_token() throws Exception {
        sendRequest(HttpMethod.POST, AUTH_API_PATH + CHECK_TOKEN)
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void checkRoles_return_200() throws Exception {
        String testRole = "TEST";

        doReturn(List.of(testRole))
                .when(authService)
                .getUserRoles(any(String.class));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + MOCK_TOKEN);

        MvcResult mvcResult = sendRequest(HttpMethod.POST, AUTH_API_PATH + CHECK_ROLES, new LinkedMultiValueMap<>(), headers, null)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        String[] foundItems = om.readValue(json, String[].class);

        assertEquals(1, foundItems.length);
        assertEquals(testRole, foundItems[0]);
    }

    @Test
    void checkRoles_return_400_no_token() throws Exception {
        sendRequest(HttpMethod.POST, AUTH_API_PATH + CHECK_ROLES)
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void getUserInfo_return_200_with_token() throws Exception {
        doReturn(UserDto.builder().id(UUID.randomUUID()).username("testUser").build())
                .when(authService)
                .getUserInfo(any(String.class));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + MOCK_TOKEN);

        sendRequest(HttpMethod.POST, AUTH_API_PATH + "/userInfo", new LinkedMultiValueMap<>(), headers, null)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUserInfo_return_400_no_token() throws Exception {
        sendRequest(HttpMethod.POST, AUTH_API_PATH + "/userInfo")
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void createUser_return_200_success() throws Exception {
        UserRepresentationDto newUser = new UserRepresentationDto(
                "testuser", "Test", "User", "test@test.com",
                true, true, "testuser", "password123");

        doReturn("new-user-id")
                .when(authService)
                .createUser(any(UserRepresentationDto.class));

        sendRequest(HttpMethod.POST, AUTH_API_PATH + "/user", newUser)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void createUser_return_500_on_service_exception() throws Exception {
        UserRepresentationDto newUser = new UserRepresentationDto(
                "testuser", "Test", "User", "test@test.com",
                true, true, "testuser", "password123");

        doThrow(new RuntimeException("Creation failed"))
                .when(authService)
                .createUser(any(UserRepresentationDto.class));

        sendRequest(HttpMethod.POST, AUTH_API_PATH + "/user", newUser)
                .andDo(print())
                .andExpect(status().is(500));
    }
}