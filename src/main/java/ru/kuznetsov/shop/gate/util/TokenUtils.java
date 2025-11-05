package ru.kuznetsov.shop.gate.util;

import com.nimbusds.jwt.JWTParser;

import java.text.ParseException;
import java.util.UUID;

public class TokenUtils {

    private static final String USER_ID_CLAIM = "sub";

    public static UUID getUserIdFromToken(String token) {
        try {
            String UserId = (String) JWTParser
                    .parse(token.replace("Bearer ", ""))
                    .getJWTClaimsSet()
                    .getClaim(USER_ID_CLAIM);

            return UUID.fromString(UserId);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
