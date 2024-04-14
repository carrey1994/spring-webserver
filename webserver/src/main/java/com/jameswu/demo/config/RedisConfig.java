package com.jameswu.demo.config;

import com.jameswu.demo.model.RedisInstance;
import java.util.List;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.connection.DnsAddressResolverGroupFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties(prefix = "redis")
@Data
public class RedisConfig {

	private RedisInstance master;
	private List<RedisInstance> slaves;
	private static final String REDIS_BASE_URL = "redis://%1s:%2s";

	@Bean
	@Profile("!test")
	public RedissonClient redisClient() {
		Config config = new Config();
		MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers()
				.setMasterAddress(String.format(REDIS_BASE_URL, master.host(), master.port()));
		config.setAddressResolverGroupFactory(new DnsAddressResolverGroupFactory());
		config.setCodec(new StringCodec());
		slaves.forEach(slave -> masterSlaveServersConfig.addSlaveAddress(
				String.format(REDIS_BASE_URL, slave.host(), slave.port())));
		return Redisson.create(config);
	}

	@Bean
	@Profile("test")
	public RedissonClient redisClientOnTest() {
		Config config = new Config();
		config.useSingleServer()
				.setAddress(String.format(REDIS_BASE_URL, master.host(), master.port()));
		config.setAddressResolverGroupFactory(new DnsAddressResolverGroupFactory());
		config.setCodec(new JsonJacksonCodec());
		return Redisson.create(config);
	}
}
