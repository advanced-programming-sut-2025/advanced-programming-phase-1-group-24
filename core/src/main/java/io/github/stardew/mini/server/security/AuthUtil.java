package io.github.stardew.mini.server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class AuthUtil {
    private static final String SECRET = "ReplaceWithYourSecretKey";
    private static final String ISSUER = "stardew-mini";

    public static String generateToken(String username) {
        return JWT.create()
            .withIssuer(ISSUER)
            .withClaim("username", username)
            .withExpiresAt(new Date(System.currentTimeMillis() + 3600_000))  // 1h expiry
            .sign(Algorithm.HMAC256(SECRET));
    }

    public static String verifyAndGetUsername(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET))
            .withIssuer(ISSUER)
            .build()
            .verify(token);
        return jwt.getClaim("username").asString();
    }
}
