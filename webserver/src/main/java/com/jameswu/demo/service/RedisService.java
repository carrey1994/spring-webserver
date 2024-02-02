package com.jameswu.demo.service;

import com.jameswu.demo.utils.GzTexts;
import com.jameswu.demo.utils.RedisKey;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    public RedisService(RedissonClient redisson) {
        this.redisson = redisson;
    }

    private final RedissonClient redisson;

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
        if ((redisPartialKey.getKey().charAt(redisPartialKey.getKey().length() - 1)) != GzTexts.DASH.charAt(0)) {
            throw new IllegalArgumentException("key is not prefix format.");
        }
        tryLock(redisPartialKey.getKey() + key);
    }

    public void tryPartialUnlock(RedisKey redisPartialKey, String key) {
        if ((redisPartialKey.getKey().charAt(redisPartialKey.getKey().length() - 1)) != GzTexts.DASH.charAt(0)) {
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

    public <T> Optional<T> getValueByKey(String key) {
        RBucket<T> data = redisson.getBucket(key);
        return Optional.of(data.get());
    }
}
