package com.jameswu.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.demo.exception.GcException;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.utils.GzTexts;
import com.jameswu.demo.utils.RedisKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtService {

	@Value("${jwt.refresh-time}")
	private int refreshTime;

	@Value("${jwt.encrypted-key}")
	private String encryptedKey;

	public static final String PROFILE = "PROFILE";
	private final RedisService redisService;
	private final ObjectMapper objectMapper;
	private final CacheService cacheService;

	public String formatBearerToken(HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		return Optional.ofNullable(authHeader)
				.map(s -> s.replace(GzTexts.BEARER_PREFIX, GzTexts.EMPTY))
				.orElse(null);
	}

	public String generateToken(GcUser user) {
		Claims claims = Jwts.claims()
				.add(PROFILE, user.getProfile())
				.expiration(new Date(System.currentTimeMillis() + refreshTime))
				.issuedAt(new Date())
				.issuer(encryptedKey)
				.build();

		String newToken = Jwts.builder()
				.json(new JacksonSerializer<>(objectMapper))
				.claims(claims)
				.signWith(secretKey())
				.compact();
		redisService.setKeyValue(RedisKey.withUserPrefix(user.getUserId()), newToken);
		return newToken;
	}

	public void removeToken(HttpServletRequest request) {
		String token = formatBearerToken(request);
		int userId = parsePayload(token, PROFILE, UserProfile.class).getUserId();
		redisService.deleteByKey(RedisKey.withUserPrefix(userId));
		cacheService.removeIdFromUserCache(userId);
	}

	public UserProfile throwIfTokenNotFound(String accessToken) {
		UserProfile userProfile = parsePayload(accessToken, PROFILE, UserProfile.class);
		int userId = userProfile.getUserId();
		if (!redisService.isKeyExists(RedisKey.withUserPrefix(userId))) {
			throw new GcException.TokenInvalidException();
		}
		return userProfile;
	}

	private SecretKey secretKey() {
		return Keys.hmacShaKeyFor(encryptedKey.getBytes());
	}

	private <T> T parsePayload(String token, String key, Class<T> clazz) {
		JwtParser parser = Jwts.parser()
				.json(new JacksonDeserializer<>(objectMapper))
				.verifyWith(secretKey())
				.build();
		Claims claims = parser.parseSignedClaims(token).getPayload();
		// TODO: Maybe JacksonDeserializer could call claims.get(key, clazz) directly
		return objectMapper.convertValue(claims.get(key, Map.class), clazz);
	}
}
