package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.repository.UserRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jameswu.demo.utils.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

	@Autowired
	private UserRepository userRepository;

	private final Map<Integer, GcUser> userCache = new ConcurrentHashMap<>();
	private final Set<Integer> specialsCache = new HashSet<>();
	private final Map<RedisKey, String> evalShaCache = new HashMap<>();

	public GcUser retrieveOrLoadUser(int userId) {
		if (userCache.containsKey(userId)) {
			return userCache.get(userId);
		} else {
			GcUser user = userRepository
					.findByUserId(userId)
					.orElseThrow(() -> new IllegalArgumentException("the user not found"));
			userCache.put(userId, user);
			return user;
		}
	}

	public void removeIdFromUserCache(int userId) {
		userCache.remove(userId);
	}

	public void addSpecialsId(int id) {
		specialsCache.add(id);
	}

	public void addEvalSha(RedisKey key, String sha) {
		evalShaCache.put(key, sha);
	}

	public String getEvalSha(RedisKey key) {
		return evalShaCache.get(key);
	}
}
