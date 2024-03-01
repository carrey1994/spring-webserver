package com.jameswu.demo.service;

import com.jameswu.demo.exception.UserException;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.repository.UserRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private UserRepository userRepository;

    private final Map<Long, GcUser> userCache = new ConcurrentHashMap<>();

    public GcUser retrieveOrLoadUser(long userId) {
        if (userCache.containsKey(userId)) {
            return userCache.get(userId);
        } else {
            GcUser user =
                    userRepository.findByUserId(userId).orElseThrow(() -> new UserException("the user not found"));
            userCache.put(userId, user);
            return user;
        }
    }

    public void removeIdFromUserCache(long userId) {
        userCache.remove(userId);
    }
}
