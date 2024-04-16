package com.jameswu.demo.config;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.enums.UserRole;
import com.jameswu.demo.model.enums.UserStatus;
import com.jameswu.demo.model.payload.SpecialsDetailPayload;
import com.jameswu.demo.repository.OrderDetailRepository;
import com.jameswu.demo.repository.OrderRepository;
import com.jameswu.demo.repository.ProductRepository;
import com.jameswu.demo.repository.UserRepository;
import com.jameswu.demo.service.RedisService;
import com.jameswu.demo.utils.RedisKey;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ConfigurationProperties(prefix = "init")
@Profile("!prod")
@Data
public class UserInitiationConfig {
	@Autowired
	public UserInitiationConfig(
			UserRepository userRepository,
			ProductRepository productRepository,
			RedisService redisService,
			BCryptPasswordEncoder passwordEncoder,
			List<InitUserData> users,
			OrderRepository orderRepository,
			OrderDetailRepository orderDetailRepository) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.redisService = redisService;
		this.passwordEncoder = passwordEncoder;
		this.users = users;
		this.orderDetailRepository = orderDetailRepository;
	}

	private UserRepository userRepository;
	private ProductRepository productRepository;
	private RedisService redisService;
	private BCryptPasswordEncoder passwordEncoder;
	private List<InitUserData> users;
	private OrderRepository orderRepository;
	private OrderDetailRepository orderDetailRepository;

	record InitUserData(
			int id, String username, String password, Integer recommenderId, UserRole role) {}

	@Bean
	public void initUsers() {
		Product a = new Product("A Ins.", "AA", BigDecimal.valueOf(100L), 100);
		Product b = new Product("B Ins.", "BB", BigDecimal.valueOf(200L), 200);
		Product c = new Product("C Ins.", "CC", BigDecimal.valueOf(300L), 300);
		var products = List.of(a, b, c);
		productRepository.saveAll(List.of(a, b, c));
		List<GcUser> gcUsers = users.stream()
				.map(user -> GcUser.builder()
						.userId(user.id)
						.userStatus(UserStatus.ACTIVE)
						.userRole(user.role)
						.username(user.username)
						.password(passwordEncoder.encode(user.password))
						.profile(new UserProfile(
								user.id,
								user.username + "@gc.mail",
								user.username,
								"Taipei",
								Instant.now(),
								user.recommenderId))
						.build())
				.toList();
		var users = userRepository.saveAll(gcUsers);
		//        for (GcUser user : users) {
		//            Order order = new Order(user, Set.of());
		//            var updatedOrder = orderRepository.save(order);
		//            orderDetailRepository.save(new OrderDetail(
		//                    products.get(new Random().nextInt(3)),
		//                    updatedOrder,
		//                    100,
		//                    BigDecimal.valueOf(100L),
		//                    UUID.randomUUID()));
		//        }

		redisService.setHashMap(RedisKey.withSpecialsPrefix(1), new SpecialsDetailPayload(100, 0));
	}
}
