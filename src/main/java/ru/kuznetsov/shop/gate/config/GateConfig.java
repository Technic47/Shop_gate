package ru.kuznetsov.shop.gate.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@RequiredArgsConstructor
@EnableScheduling
@EnableConfigurationProperties(PermissionsConfig.class)
public class GateConfig {

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

}
