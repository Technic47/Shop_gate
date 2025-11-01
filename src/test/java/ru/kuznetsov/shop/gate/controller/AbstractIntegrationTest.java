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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.kuznetsov.shop.gate.dto.LoginPasswordDto;
import ru.kuznetsov.shop.gate.dto.TokenDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    protected TokenDto getToken(String login, String pass) throws Exception {
        LoginPasswordDto request = new LoginPasswordDto(login, pass);

        MvcResult mvcResult = sendRequest(HttpMethod.POST, AUTH_API_PATH, request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        return om.readValue(json, TokenDto.class);
    }

    protected ResultActions sendRequestWithAuthToken(HttpMethod httpMethod, String apiPath, String login, String pass) throws Exception {
        TokenDto response = getToken(login, pass);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());

        return sendRequest(httpMethod, apiPath, headers, null);
    }

    protected ResultActions sendRequestWithAuthToken(HttpMethod httpMethod, String apiPath, Object body, String login, String pass) throws Exception {
        TokenDto response = getToken(login, pass);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());

        return sendRequest(httpMethod, apiPath, headers, body);
    }

    protected ResultActions sendRequest(HttpMethod httpMethod, String apiPath) throws Exception {
        return sendRequest(httpMethod, apiPath, new HttpHeaders(), null);
    }

    protected ResultActions sendRequest(HttpMethod httpMethod, String apiPath, Object body) throws Exception {
        return sendRequest(httpMethod, apiPath, new HttpHeaders(), body);
    }

    protected ResultActions sendRequest(HttpMethod httpMethod, String apiPath, HttpHeaders httpHeaders, Object body) throws Exception {
        return mockMvc.perform(request(httpMethod, apiPath)
                .headers(httpHeaders)
                .content(om.writeValueAsString(body))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }
}
