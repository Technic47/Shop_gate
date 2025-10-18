package ru.kuznetsov.shop.gate.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

import static ru.kuznetsov.shop.gate.common.ConstValues.*;


@Configuration
@RequiredArgsConstructor
@EnableScheduling
@EnableConfigurationProperties(PermissionsConfig.class)
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
    @Qualifier("address")
    public WebClient getAddressClient() {
        return getWebClient(ADDRESS_PORT);
    }

    @Bean
    @Qualifier("product")
    public WebClient getProductClient() {
        return getWebClient(PRODUCT_PORT);
    }

    @Bean
    @Qualifier("product-category")
    public WebClient getProductCategoryClient() {
        return getWebClient(PRODUCT_CATEGORY_PORT);
    }

    @Bean
    @Qualifier("stock")
    public WebClient getStockClient() {
        return getWebClient(STOCK_PORT);
    }

    @Bean
    @Qualifier("store")
    public WebClient getStoreClient() {
        return getWebClient(STORE_PORT);
    }

    private WebClient getWebClient(String port){
        return WebClient.builder()
                .baseUrl(baseUrl + ":" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
