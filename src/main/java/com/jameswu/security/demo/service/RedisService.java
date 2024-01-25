package com.jameswu.security.demo.service;

import java.util.concurrent.TimeUnit;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedissonClient redissonClient;

    public RLock tryLock() {
        RLock rLock = redissonClient.getLock("hello");
        rLock.lock(15, TimeUnit.SECONDS);
        return rLock;
    }

    public void unlock(RLock rLock) {
        rLock.unlock();
    }
}
