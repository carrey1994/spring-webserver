package com.jameswu.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.UserProfile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

	@Value("${jwt.refresh-time}")
	private int refreshTime;

	private static final String JWT_KEY = "xVcqqzLRUSnYUKgAciPKnAqgrHGpDLmnEiuLXHeHqBiFHJpQ";
	public static final String JWT_USER = "USER";
	private final RedisService redisService;
	private final ObjectMapper objectMapper;
	private final SecretKey secretKey = Keys.hmacShaKeyFor(JWT_KEY.getBytes());

	@Autowired
	public JwtService(RedisService redisService, ObjectMapper objectMapper) {
		this.redisService = redisService;
		this.objectMapper = objectMapper;
	}

	public String generateToken(GcUser user) {
		Claims claims =
				Jwts.claims()
						.add(JWT_USER, user.getProfile())
						.expiration(new Date(System.currentTimeMillis() + refreshTime))
						.issuedAt(new Date((System.currentTimeMillis())))
						.issuer(JWT_KEY)
						.build();

		String newToken =
				Jwts.builder()
						.json(new JacksonSerializer<>(objectMapper))
						.claims(claims)
						.signWith(secretKey)
						.compact();
		redisService.setKeyValue(String.valueOf(user.getUserId()), newToken);
		return newToken;
	}

	public <T> T parsePayload(String token, String key, Class<T> clazz) {
		JwtParser parser =
				Jwts.parser()
						.json(new JacksonDeserializer<>(objectMapper))
						.verifyWith(secretKey)
						.build();
		Claims claims = parser.parseSignedClaims(token).getPayload();
		// TODO: fix JacksonDeserializer to call claims.get(key, clazz) directly
		return objectMapper.convertValue(claims.get(key, Map.class), clazz);
	}

	public void removeToken(String token) {
		String userId =
				String.valueOf(parsePayload(token, JWT_USER, UserProfile.class).getUserId());
		redisService.deleteByKey(userId);
	}
}
