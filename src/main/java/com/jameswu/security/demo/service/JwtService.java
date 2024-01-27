package com.jameswu.security.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.security.demo.model.GcUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

    private final String KEY = "xVcqqzLRUSnYUKgAciPKnAqgrHGpDLmnEiuLXHeHqBiFHJpQ";

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper mapper;

    public String generateToken(GcUser user) {

        Claims claims = Jwts.claims()
                .add("user_id", user.getUserId().toString())
                .expiration(new Date(System.currentTimeMillis() + 24 * 1000 * 60))
                .issuedAt(new Date((System.currentTimeMillis())))
                .issuer(KEY)
                .build();

        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());
        String newToken = Jwts.builder().claims(claims).signWith(secretKey).compact();
        redisService.setKeyValue(user.getUserId().toString(), newToken);
        return newToken;
    }

    public Map<String, Object> parseToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(KEY.getBytes());
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = parser.parseSignedClaims(token).getPayload();
        return claims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
