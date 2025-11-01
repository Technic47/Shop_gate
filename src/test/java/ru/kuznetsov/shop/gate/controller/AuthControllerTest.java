package ru.kuznetsov.shop.gate.controller;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MvcResult;
import ru.kuznetsov.shop.gate.dto.LoginPasswordDto;
import ru.kuznetsov.shop.gate.dto.TokenDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Order(1)
class AuthControllerTest extends AbstractIntegrationTest {

    protected final static String CHECK_TOKEN = "/check/token";
    protected final static String CHECK_ROLES = "/check/roles";

    @Test
    void getToken_return_200() throws Exception {
        TokenDto response = getToken(TEST_USER_LOGIN, TEST_USER_PASSWORD);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getTokenType());
        assertNotNull(response.getSessionState());
        assertNotNull(response.getScope());
        assertNull(response.getError());
        assertNull(response.getErrorDescription());
        assertTrue(response.getExpiresIn() > 0);

        assertDoesNotThrow(() -> JWTParser.parse(response.getToken().replace("Bearer ", "")));

        Map<String, Object> claimsSet = JWTParser.parse(response.getToken().replace("Bearer ", "")).getJWTClaimsSet().getClaims();

        assertTrue(((Date) claimsSet.get("exp")).after(new Date()));
    }

    @Test
    void getToken_return_401_wrong_login() throws Exception {
        LoginPasswordDto request = new LoginPasswordDto(TEST_USER_LOGIN + "123", TEST_USER_PASSWORD);

        sendRequest(HttpMethod.POST, AUTH_API_PATH, request)
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void checkToken_return_true() throws Exception {
        TokenDto response = getToken(TEST_USER_LOGIN, TEST_USER_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());

        MvcResult mvcResult = sendRequest(HttpMethod.POST, AUTH_API_PATH + CHECK_TOKEN, headers, null)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(Boolean.parseBoolean(mvcResult.getResponse().getContentAsString()));
    }

//    @Test
//    void checkToken_return_false() throws Exception {
//        TokenDto response = getToken(TEST_USER_LOGIN, TEST_USER_PASSWORD);
//
//        JWT jwt = JWTParser.parse(response.getToken().replace("Bearer ", ""));
//        Map<String, Object> claimsSet = jwt.getJWTClaimsSet().getClaims();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());
//
//        MvcResult mvcResult = sendRequest(HttpMethod.POST, AUTH_API_PATH + CHECK_TOKEN, headers, null)
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        assertTrue(Boolean.parseBoolean(mvcResult.getResponse().getContentAsString()));
//    }

    @Test
    void checkRoles() throws Exception {
        TokenDto response = getToken(TEST_USER_LOGIN, TEST_USER_PASSWORD);

        JWT jwt = JWTParser.parse(response.getToken().replace("Bearer ", ""));
        String[] roleList = ((Map<String, List<String>>) jwt.getJWTClaimsSet().getClaim("realm_access"))
                .get("roles")
                .stream()
                .filter(role -> role.startsWith("ROLE_"))
                .map(role -> role.substring(5))
                .toList()
                .toArray(new String[0]);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());

        MvcResult mvcResult = sendRequest(HttpMethod.POST, AUTH_API_PATH + CHECK_ROLES, headers, null)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        String[] foundItems = om.readValue(json, String[].class);

        assertArrayEquals(roleList, foundItems);
    }
}