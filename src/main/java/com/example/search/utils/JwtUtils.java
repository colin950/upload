package com.example.search.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtils implements Serializable {
    // ㅎㅐ당 값을 지정하지 않으면 compiler마다 다른 값이 될 수 있다.
    private static final long serialVersionUID = -23452345656345234L;

    private String secret = "colin";

    private Long JWT_TOKEN_VALIDITY = 3600L;

    public Claims getAllClaimsFromToken(String token) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            String n = "vgdvfgvdfv";
            String e = "AQAB";
            BigInteger modules = new BigInteger(1, Base64.getUrlDecoder().decode(n));
            BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));
            PublicKey publicKey =
                    keyFactory.generatePublic(new RSAPublicKeySpec(modules, exponent));

            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            // TODO auto generate catch block
//            throw new RuntimeException(e);
        }
        return null;
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getClaimsFromToken(String token) throws IOException {
        String[] jwt = token.split("\\.");

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(jwt[1].getBytes());

        Map<String, Object> map = new ObjectMapper().readValue(decodedBytes, Map.class);
        return Jwts.claims(map);
    }

}
