package ru.kuznetsov.shop.gate.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@RequiredArgsConstructor
public class GateConfig {

    @Value("${microservices.baseUrl}")
    private String baseUrl;

    private final KeycloakConfiguration config;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .serverUrl(config.getServerUrl())
                .realm(config.getRealm())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .build();
    }

//    @Bean
//    public Keycloak keycloak() {
//        return KeycloakBuilder.builder()
//                .grantType(OAuth2Constants.PASSWORD)
//                .serverUrl(config.getServerUrl())
//                .realm(config.getRealm())
//                .clientId(config.getClientId())
//                .username(config.getUsername())
//                .password(config.getPassword())
//                .build();
//    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
