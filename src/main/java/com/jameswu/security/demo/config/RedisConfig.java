package com.jameswu.security.demo.config;

import com.jameswu.security.demo.model.RedisInstance;
import java.util.List;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis")
@Data
public class RedisConfig {

    private RedisInstance master;
    private List<RedisInstance> slaves;
    private final String REDIS_BASE_URL_PATTERN = "redis://%1s:%2s";

    @Bean
    public RedissonClient redisClient() {
        Config config = new Config();
        MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers()
                .setMasterAddress(String.format(REDIS_BASE_URL_PATTERN, master.getHost(), master.getPort()));
        slaves.forEach(slave -> {
            masterSlaveServersConfig.addSlaveAddress(
                    String.format(REDIS_BASE_URL_PATTERN, slave.getHost(), slave.getPort()));
        });
        return Redisson.create(config);
    }
}
