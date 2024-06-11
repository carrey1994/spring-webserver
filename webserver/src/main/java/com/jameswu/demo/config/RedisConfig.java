package com.jameswu.demo.config;

import com.jameswu.demo.model.RedisInstance;
import java.util.List;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
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
	@Profile("!supa")
	public RedissonClient masterSlaveRedisClient() {
		Config config = defaultConfig();
		MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers()
				.setMasterAddress(String.format(REDIS_BASE_URL, master.host(), master.port()));
		slaves.forEach(slave ->
				masterSlaveServersConfig.addSlaveAddress(String.format(REDIS_BASE_URL, slave.host(), slave.port())));
		return Redisson.create(config);
	}

	@Bean
	@Profile("supa")
	public RedissonClient singleClientOnTest() {
		Config config = defaultConfig();
		config.useSingleServer()
				.setAddress(String.format(REDIS_BASE_URL, master.host(), master.port()))
				.setPassword(master.password());
		return Redisson.create(config);
	}

	public Config defaultConfig() {
		Config config = new Config();
		config.setAddressResolverGroupFactory(new DnsAddressResolverGroupFactory());
		config.setCodec(new StringCodec());
		return config;
	}
}
