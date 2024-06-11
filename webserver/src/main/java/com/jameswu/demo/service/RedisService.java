package com.jameswu.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.demo.utils.LuaScripts;
import com.jameswu.demo.utils.RedisKey;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

	@Autowired
	public RedisService(RedissonClient redisson, CacheService cacheService, ObjectMapper objectMapper) {
		this.redisson = redisson;
		this.cacheService = cacheService;
		this.objectMapper = objectMapper;
	}

	private final CacheService cacheService;
	private final RedissonClient redisson;
	private final ObjectMapper objectMapper;

	@PostConstruct
	public void init() {
		String evalSha = redisson.getScript().scriptLoad(LuaScripts.BOOKING_SPECIALS_LUA_script);
		cacheService.addEvalSha(RedisKey.LUA_CREATE_SPECIALS_ORDER, evalSha);
	}

	public void setKeyValue(String key, String value) {
		redisson.getBucket(key).set(value);
	}

	public void deleteByKey(String key) {
		redisson.getBucket(key).getAndDelete();
	}

	public boolean isKeyExists(String key) {
		return redisson.getBucket(key).isExists();
	}

	public Map<String, String> setHashMap(String key, Object object) {
		Map map = objectMapper.convertValue(object, Map.class);
		redisson.getMap(key).putAll(map);
		return map;
	}

	public <T> T getHashClass(String hashKey, Class<T> clazz) {
		return objectMapper.convertValue(redisson.getBucket(hashKey).get(), clazz);
	}

	public <T> Optional<T> getValueByKey(String key) {
		RBucket<T> data = redisson.getBucket(key);
		return Optional.ofNullable(data.get());
	}
}
