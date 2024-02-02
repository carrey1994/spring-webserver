package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.GcUser;
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

    private static final String JWT_KEY = "xVcqqzLRUSnYUKgAciPKnAqgrHGpDLmnEiuLXHeHqBiFHJpQ";
    public static final String USER_ID = "user_id";
    private final RedisService redisService;

    @Autowired
    public JwtService(RedisService redisService) {
        this.redisService = redisService;
    }

    public String generateToken(GcUser user) {

        Claims claims = Jwts.claims()
                .add(USER_ID, String.valueOf(user.getUserId()))
                .expiration(new Date(System.currentTimeMillis() + 10 * 1000 * 60))
                .issuedAt(new Date((System.currentTimeMillis())))
                .issuer(JWT_KEY)
                .build();

        Key secretKey = Keys.hmacShaKeyFor(JWT_KEY.getBytes());
        String newToken = Jwts.builder().claims(claims).signWith(secretKey).compact();
        redisService.setKeyValue(String.valueOf(user.getUserId()), newToken);
        return newToken;
    }

    public Map<String, Object> parseToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(JWT_KEY.getBytes());
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = parser.parseSignedClaims(token).getPayload();
        return claims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void removeToken(String token) {
        String userId = parseToken(token).get(USER_ID).toString();
        redisService.deleteByKey(userId);
    }

    public void checkToken(String token) {
        String userId = parseToken(token).get(USER_ID).toString();
        redisService.getValueByKey(userId).ifPresentOrElse(e -> {}, () -> {
            throw new IllegalArgumentException("token not found");
        });
    }
}
