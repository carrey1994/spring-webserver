package com.jameswu.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.demo.utils.GzTexts;
import com.jameswu.demo.utils.RedisKey;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

	@Autowired
	public RedisService(
			RedissonClient redisson, CacheService cacheService, ObjectMapper objectMapper) {
		this.redisson = redisson;
		this.cacheService = cacheService;
		this.objectMapper = objectMapper;
	}

	private final CacheService cacheService;
	private final RedissonClient redisson;
	private final ObjectMapper objectMapper;

	public void trySystemLock(RedisKey systemKey) {
		if ((systemKey.getKey().charAt(systemKey.getKey().length() - 1)) == '_') {
			throw new IllegalArgumentException("key is not system format.");
		}
		tryLock(systemKey.getKey());
	}

	public void trySystemUnlock(RedisKey redisSystemKey) {
		if ((redisSystemKey.getKey().charAt(redisSystemKey.getKey().length() - 1)) == '_') {
			throw new IllegalArgumentException("key is not system format.");
		}
		RLock lock = redisson.getLock(redisSystemKey.getKey());
		if (lock.isLocked()) {
			lock.unlock();
		}
	}

	public void tryPartialLock(RedisKey redisPartialKey, String key) {
		if ((redisPartialKey.getKey().charAt(redisPartialKey.getKey().length() - 1))
				!= GzTexts.DASH.charAt(0)) {
			throw new IllegalArgumentException("key is not prefix format.");
		}
		tryLock(redisPartialKey.getKey() + key);
	}

	public void tryPartialUnlock(RedisKey redisPartialKey, String key) {
		if ((redisPartialKey.getKey().charAt(redisPartialKey.getKey().length() - 1))
				!= GzTexts.DASH.charAt(0)) {
			throw new IllegalArgumentException("key is not prefix format.");
		}
		RLock lock = redisson.getLock(redisPartialKey.getKey() + key);
		if (lock.isLocked()) {
			lock.unlock();
		}
	}

	private void tryLock(String key) {
		RLock rLock = redisson.getLock(key);
		if (!rLock.isLocked()) {
			rLock.lock(15, TimeUnit.SECONDS);
		}
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
		redisson.getBucket(key).set(map);
		return map;
	}

	public void setHashMap(){
		redisson.getBucket("SPECIALS_PRODUCT_ID_1").set(Map.of("inventory", "100", "booked", "0"));
	}

	public <T> T getHashClass(String hashKey, Class<T> clazz) {
		return objectMapper.convertValue(redisson.getBucket(hashKey).get(), clazz);
	}

	public String loadLuaScript(String lua) {
		String evalSha = redisson.getScript().scriptLoad(lua);
		cacheService.addEvalSha(RedisKey.LUA_CREATE_SPECIALS_ORDER, evalSha);
		return evalSha;
	}

	public void executeEvalSha() {
		String evalSha = cacheService.getEvalSha(RedisKey.LUA_CREATE_SPECIALS_ORDER);
		redisson.getScript()
				.evalSha(
						RScript.Mode.READ_WRITE,
						evalSha,
						RScript.ReturnType.VALUE,
						List.of(1, RedisKey.withSpecialsPrefix(1), 1));
	}

	public <T> Optional<T> getValueByKey(String key) {
		RBucket<T> data = redisson.getBucket(key);
		return Optional.of(data.get());
	}
}
