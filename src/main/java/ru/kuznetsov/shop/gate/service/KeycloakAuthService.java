package ru.kuznetsov.shop.gate.service;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.kuznetsov.shop.business.access.service.OperationService;
import ru.kuznetsov.shop.gate.config.KeycloakConfiguration;
import ru.kuznetsov.shop.gate.dto.LoginPasswordDto;
import ru.kuznetsov.shop.gate.dto.TokenDto;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakAuthService implements AuthService {

    private static final String REALMS = "/realms";
    private static final String PROTOCOL = "/protocol";
    private static final String OPENID_CONNECT = "/openid-connect";
    private static final String TOKEN = "/token";
    private static final String INTROSPECT = "/introspect";
    private static final String ROLE_ADMIN = "ADMIN";

    private final KeycloakConfiguration config;

    Logger logger = LoggerFactory.getLogger(OperationService.class);

    public ResponseEntity<TokenDto> getToken(LoginPasswordDto authHeader) {
        Keycloak client = getConfidentialClient(authHeader);
        try {
            AccessTokenResponse accessToken = client.tokenManager().getAccessToken();
            return ResponseEntity.ok(TokenDto.builder()
                    .token(accessToken.getToken())
                    .expiresIn(accessToken.getExpiresIn())
                    .refreshToken(accessToken.getRefreshToken())
                    .refreshExpiresIn(accessToken.getRefreshExpiresIn())
                    .tokenType(accessToken.getTokenType())
                    .sessionState(accessToken.getSessionState())
                    .otherClaims(accessToken.getOtherClaims())
                    .scope(accessToken.getScope())
                    .error(accessToken.getError())
                    .errorDescription(accessToken.getErrorDescription())
                    .build());
        } catch (Exception e) {
            logger.error("getToken", e);
            return ResponseEntity.status(401).build();
        }
    }

    @Override
    public Boolean isTokenValid(String token) {
        String introspectSubPath = REALMS + "/" + config.getRealm() + PROTOCOL + OPENID_CONNECT + TOKEN + INTROSPECT;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> introspectParams = new LinkedMultiValueMap<>();
        introspectParams.add("client_id", config.getClientId());
        introspectParams.add("token", token.replace("Bearer ", ""));
        introspectParams.add("client_secret", config.getClientSecret());

        var request = new HttpEntity<>(introspectParams, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.postForEntity(
                config.getServerUrl() + introspectSubPath,
                request,
                Map.class
        );

        logger.debug("Got response from keycloak: {}", response.getBody());

        return response.getBody() != null && (Boolean) response.getBody().get("active");
    }

    public boolean checkIfUserIsAdmin(String token) {
        return checkIfUserHasRole(token, ROLE_ADMIN);
    }

    public boolean checkIfUserHasRole(String token, String role) {
        return getUserRoles(token).contains(role);
    }

    @Override
    public Collection<String> getUserRoles(String token) {
        try {
            JWT jwt = JWTParser.parse(token.replace("Bearer ", ""));
            return ((Map<String, List<String>>) jwt.getJWTClaimsSet().getClaim("realm_access"))
                    .get("roles")
                    .stream()
                    .filter(role -> role.startsWith("ROLE_"))
                    .map(role -> role.substring(5))
                    .toList();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Keycloak getConfidentialClient(LoginPasswordDto authHeader) {
        return KeycloakBuilder.builder()
                .grantType(OAuth2Constants.PASSWORD)
                .serverUrl(config.getServerUrl())
                .realm(config.getRealm())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .username(authHeader.getLogin())
                .password(authHeader.getPassword())
                .build();
    }
}
