package com.jameswu.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.demo.utils.GzTexts;
import com.jameswu.demo.utils.RedisKey;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RSearch;
import org.redisson.api.RedissonClient;
import org.redisson.api.search.index.FieldIndex;
import org.redisson.api.search.index.IndexOptions;
import org.redisson.api.search.index.IndexType;
import org.redisson.api.search.query.QueryOptions;
import org.redisson.api.search.query.ReturnAttribute;
import org.redisson.api.search.query.SearchResult;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.CompositeCodec;
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

	public RMap<String, String> setHashMap(String key, Object object) {
		Map map = objectMapper.convertValue(object, Map.class);
		RMap<String, String> redissonMap = redisson.getMap(key);
		redissonMap.putAll(map);
		return redissonMap;
	}

	public <T> T getHashMap(String hashKey, String fieldKey) {
		return (T) redisson.getMap(hashKey).get(fieldKey);
	}

	public <T> T getHashClass(String hashKey, Class<T> clazz) {
		RMap<Object, Object> map = redisson.getMap(hashKey);
		return objectMapper.convertValue(map, clazz);
	}

	public void redisIdx() {

		Codec codec = new CompositeCodec(StringCodec.INSTANCE, redisson.getConfig().getCodec());

		RMap m = redisson.getMap("doc:1", codec);
		//        m.put("inventory", new SimpleObject("name1"));
		//        m.put("v2", new SimpleObject("name2"));
		//
		//        RMap m2 = redisson.getMap("doc:2", codec);
		//        m2.put("v1", new SimpleObject("name3"));
		//        m2.put("v2", new SimpleObject("name4"));

		RSearch s = redisson.getSearch();
		s.createIndex(
				"idx",
				IndexOptions.defaults()
						.on(IndexType.HASH)
						.prefix(Collections.singletonList(RedisKey.PREFIX_SPECIALS.getKey())),
				FieldIndex.text("inventory"),
				FieldIndex.text("booked"));

		SearchResult r =
				s.search(
						"idx",
						"*",
						QueryOptions.defaults()
								.returnAttributes(
										new ReturnAttribute("inventory"),
										new ReturnAttribute("booked")));
		System.out.println();

		//        RSearch s = redisson.getSearch();
		//        s.createIndex("idx", IndexOptions.defaults()
		//                        .on(IndexType.HASH)
		//                        .prefix(List.of(RedisKey.PREFIX_SPECIALS.getKey())),
		//                FieldIndex.text("inventory"),
		//                FieldIndex.text("booked"));
		//
		//        SearchResult r = s.search("idx", "*", QueryOptions.defaults()
		//                .returnAttributes(new ReturnAttribute("inventory"), new
		// ReturnAttribute("booked")));
		//        System.out.println();
	}

	//    public <T> T getHashClassList(String hashKey, Class<T> clazz) {
	//        RMap<Object, Object> map = redisson.getList(hashKey);
	//        objectMapper.convertValue(jsonString,
	// objectMapper.getTypeFactory().constructCollectionType(List.class, MyPOJO.class)
	//        return objectMapper.convertValue(map, clazz);
	//    }

	public <T> Optional<T> getValueByKey(String key) {
		RBucket<T> data = redisson.getBucket(key);
		return Optional.of(data.get());
	}
}
