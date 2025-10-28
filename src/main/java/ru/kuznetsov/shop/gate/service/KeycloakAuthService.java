package ru.kuznetsov.shop.gate.service;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kuznetsov.shop.business.access.service.OperationService;
import ru.kuznetsov.shop.gate.config.KeycloakConfiguration;
import ru.kuznetsov.shop.gate.dto.LoginPasswordDto;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakAuthService implements AuthService {

    private static final String REALMS = "/realms";
    private static final String SHOP_REALM = "/shop";
    private static final String PROTOCOL = "/protocol";
    private static final String OPENID_CONNECT = "/openid-connect";
    private static final String TOKEN = "/token";
    private static final String ROLE_ADMIN = "ADMIN";

    private final KeycloakConfiguration config;

    private final Keycloak keycloakClient;
    Logger logger = LoggerFactory.getLogger(OperationService.class);

    public List<UserRepresentation> getInfo(String userName) {

        AccessTokenResponse accessToken = keycloakClient.tokenManager().getAccessToken();

        try {
            JWT jwt = JWTParser.parse(accessToken.getToken());
            List<String> roles = ((Map<String, List<String>>) jwt.getJWTClaimsSet().getClaim("realm_access"))
                    .get("roles")
                    .stream()
                    .filter(role -> role.startsWith("ROLE_"))
                    .toList();
            System.out.println(roles);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<UserRepresentation> list = keycloakClient.realm(config.getRealm()).users().search(userName, false);
        Map<String, List<String>> groups = list.get(0).getClientRoles();
        return list;
    }

    public String loginAuth(LoginPasswordDto authHeader) {
        return keycloakClient.tokenManager().getAccessToken().getToken();
    }

    public boolean checkIfUserIsAdmin(String token) {
        return checkIfUserHasRole(token, ROLE_ADMIN);
    }

    public boolean checkIfUserHasRole(String token, String role) {
        return getUserRoles(token).contains(role);
    }

    public List<String> getUserRoles(String token) {
        try {
            JWT jwt = JWTParser.parse(token);
            return ((Map<String, List<String>>) jwt.getJWTClaimsSet().getClaim("realm_access"))
                    .get("roles")
                    .stream()
                    .filter(role -> role.startsWith("ROLE_"))
                    .map(role -> role.substring(4))
                    .toList();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
