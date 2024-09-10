package com.recipes.utils;

import io.jsonwebtoken.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * Generate JWT
     * Using Hs256 algorithm, the private key uses a fixed secret key
     *
     * @param secretKey jwt secret key
     * @param ttlMillis jwt expiration time (milliseconds)
     * @param claims    information to set
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // Specify the signature algorithm used for signing, which is the header part
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // Generate the time for JWT
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // Set the body of the jwt
        JwtBuilder builder = Jwts.builder()
                // If there are private claims, you must set this private claim first. This is to assign values to the builder's claim. Once written after the standard claim assignment, it will overwrite those standard claims.
                .setClaims(claims)
                // Set the signature algorithm and the secret key used for signing
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // Set the expiration time
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Decrypt the token
     *
     * @param secretKey jwt secret key. This key must be kept on the server side and should not be exposed, otherwise the sign can be forged. If multiple clients are interfaced, it is recommended to transform into multiple keys.
     * @param token     encrypted token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        try {
            // Print the received token
            System.out.println("Received token: " + token);

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();

            // Print the claims
            System.out.println("Token claims: " + claims);

            return claims;
        } catch (ExpiredJwtException e) {
            // Handling expired tokens
            System.out.println("Token has expired");
            throw e;
        } catch (Exception e) {
            // Handling exception
            System.out.println("Token is invalid: " + e.getMessage());
            throw e;
        }
    }

}
