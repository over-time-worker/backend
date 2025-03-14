package com.owlexpress.auth.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    private final String AUTHORIZATION_KEY = "auth";
    private final String ISSUER = "OvO";

    private final SecretKey key;
    private final Integer accessExp;
    private final Integer refreshExp;

    public TokenProvider(
            @Value("${jwt.secret}") String salt,
            @Value("${jwt.access.exp}") Integer accessExp,
            @Value("${jwt.refresh.exp}") Integer refreshExp
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(salt));
        this.accessExp = accessExp;
        this.refreshExp = refreshExp;
    }


    public String generateAccessToken(Long userId, String authorities) {
        Date now = new Date();

        return Jwts.builder()
                .subject(Long.toString(userId))
                .claim(AUTHORIZATION_KEY, authorities)
                .issuer(ISSUER)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessExp))
                .signWith(key, SIG.HS512)
                .compact();
    }

    public String generateRefreshToken(Long userId, String authorities) {
        Date now = new Date();

        return Jwts.builder()
                .subject(Long.toString(userId))
                .claim(AUTHORIZATION_KEY, authorities)
                .issuer(ISSUER)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshExp))
                .signWith(key, SIG.HS512)
                .compact();
    }

}
