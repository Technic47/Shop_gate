package ru.kuznetsov.shop.gate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.kuznetsov.shop.represent.contract.auth.AuthContract;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public abstract class AbstractIntegrationTest {

    protected final static String AUTH_API_PATH = "/auth";
    protected final static String TEST_USER_LOGIN = "shop_test";
    protected final static String TEST_USER_PASSWORD = "test";
    protected final static String TEST_ADMIN_LOGIN = "shop_test_admin";
    protected final static String TEST_ADMIN_PASSWORD = "admin";

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper om;

    @MockitoBean
    protected AuthContract authService;

    protected TokenDto getToken(String login, String pass) {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(login + ":" + pass);

        return tokenDto;
    }

    protected ResultActions sendRequestWithAuthToken(HttpMethod httpMethod, String apiPath, String login, String pass) throws Exception {
        TokenDto response = getToken(login, pass);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());

        return sendRequest(httpMethod, apiPath, new LinkedMultiValueMap<>(), headers, null);
    }

    protected ResultActions sendRequestWithAuthToken(HttpMethod httpMethod, String apiPath, Object body, String login, String pass) throws Exception {
        TokenDto response = getToken(login, pass);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());

        return sendRequest(httpMethod, apiPath, new LinkedMultiValueMap<>(), headers, body);
    }

    protected ResultActions sendRequestWithAuthToken(HttpMethod httpMethod, String apiPath, MultiValueMap<String, String> params, Object body, String login, String pass) throws Exception {
        TokenDto response = getToken(login, pass);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());

        return sendRequest(httpMethod, apiPath, params, headers, body);
    }

    protected ResultActions sendRequest(HttpMethod httpMethod, String apiPath) throws Exception {
        return sendRequest(httpMethod, apiPath, new LinkedMultiValueMap<>(), new HttpHeaders(), null);
    }

    protected ResultActions sendRequest(HttpMethod httpMethod, String apiPath, MultiValueMap<String, String> params) throws Exception {
        return sendRequest(httpMethod, apiPath, params, new HttpHeaders(), null);
    }

    protected ResultActions sendRequest(HttpMethod httpMethod, String apiPath, Object body) throws Exception {
        return sendRequest(httpMethod, apiPath, new LinkedMultiValueMap<>(), new HttpHeaders(), body);
    }

    protected ResultActions sendRequest(HttpMethod httpMethod, String apiPath, MultiValueMap<String, String> params, Object body) throws Exception {
        return sendRequest(httpMethod, apiPath, params, new HttpHeaders(), body);
    }

    protected ResultActions sendRequest(HttpMethod httpMethod, String apiPath, MultiValueMap<String, String> params, HttpHeaders httpHeaders, Object body) throws Exception {
        return mockMvc.perform(request(httpMethod, apiPath)
                .params(params)
                .headers(httpHeaders)
                .content(om.writeValueAsString(body))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }
}
