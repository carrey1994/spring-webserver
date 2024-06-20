package com.jameswu.demo.config;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.enums.UserRole;
import com.jameswu.demo.model.enums.UserStatus;
import com.jameswu.demo.repository.OrderDetailRepository;
import com.jameswu.demo.repository.OrderRepository;
import com.jameswu.demo.repository.ProductRepository;
import com.jameswu.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
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
public class MockDataConfig {
	@Autowired
	public MockDataConfig(
			UserRepository userRepository,
			ProductRepository productRepository,
			BCryptPasswordEncoder passwordEncoder,
			List<InitUserData> users,
			OrderRepository orderRepository,
			OrderDetailRepository orderDetailRepository) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.passwordEncoder = passwordEncoder;
		this.users = users;
		this.orderDetailRepository = orderDetailRepository;
	}

	private UserRepository userRepository;
	private ProductRepository productRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private List<InitUserData> users;
	private OrderRepository orderRepository;
	private OrderDetailRepository orderDetailRepository;

	record InitUserData(int id, String username, String password, Integer recommenderId, UserRole role) {}

	@Bean
	@Transactional
	public void init() {
		// mock products
		List<Product> products = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			products.add(new Product("A Ins.", "AA", BigDecimal.valueOf(i + 1), i + 1));
		}
		productRepository.saveAll(products);

		// mock users
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
		userRepository.saveAll(gcUsers);
	}
}
