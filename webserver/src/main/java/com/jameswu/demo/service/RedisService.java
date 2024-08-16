package com.jameswu.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.demo.utils.LuaScripts;
import com.jameswu.demo.utils.RedisKey;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisService {

	private final CacheService cacheService;
	private final RedissonClient redissonClient;
	private final ObjectMapper objectMapper;

	@PostConstruct
	public void init() {
		String evalSha = redissonClient.getScript().scriptLoad(LuaScripts.BOOKING_SPECIALS_LUA_script);
		cacheService.addEvalSha(RedisKey.LUA_CREATE_SPECIALS_ORDER, evalSha);
	}

	public void setKeyValue(String key, String value) {
		redissonClient.getBucket(key).set(value);
	}

	public void deleteByKey(String key) {
		redissonClient.getBucket(key).getAndDelete();
	}

	public boolean isKeyExists(String key) {
		return redissonClient.getBucket(key).isExists();
	}

	public Map<String, String> setHashMap(String key, Object object) {
		Map map = objectMapper.convertValue(object, Map.class);
		redissonClient.getMap(key).putAll(map);
		return map;
	}

	public <T> T getHashClass(String hashKey, Class<T> clazz) {
		return objectMapper.convertValue(redissonClient.getBucket(hashKey).get(), clazz);
	}

	public <T> Optional<T> getValueByKey(String key) {
		RBucket<T> data = redissonClient.getBucket(key);
		return Optional.ofNullable(data.get());
	}

	public void executeDuringLocked(RedisKey prefix, List<String> ids, Runnable runnable) {
		List<RLock> locks =
				ids.stream().map(id -> redissonClient.getLock(prefix + id)).toList();
		try {
			boolean allLocksAcquired = makeLock(prefix, ids);
			if (allLocksAcquired) {
				runnable.run();
			} else {
				throw new RuntimeException("Could not acquire all locks");
			}
		} finally {
			locks.forEach(RLock::unlock);
		}
	}

	private boolean makeLock(RedisKey prefix, List<String> ids) {
		List<RLock> locks =
				ids.stream().map(id -> redissonClient.getLock(prefix + id)).toList();
		return locks.stream().allMatch(lock -> {
			try {
				return lock.tryLock(100, 30, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				return false;
			}
		});
	}
}
