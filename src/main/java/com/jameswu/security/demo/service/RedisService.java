package com.jameswu.security.demo.service;

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
    private RedissonClient redisson;

    public RLock tryLock(String key) {
        RLock rLock = redisson.getLock(key);
        rLock.lock(15, TimeUnit.SECONDS);
        return rLock;
    }

    public void unlock(RLock rLock) {
        rLock.unlock();
    }

    public void setKeyValue(String key, String value) {
        redisson.getBucket(key).set(value);
    }

    public void deleteByKey(String key) {
        redisson.getBucket(key).getAndDelete();
    }

    public <T> Optional<T> getValueByKey(String key) {
        RBucket<T> data = redisson.getBucket(key);
        Optional<T> result = Optional.of(data.get());
        return result;
    }
}
