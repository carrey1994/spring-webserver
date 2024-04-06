package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.Comment;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.payload.CommentPayload;
import com.jameswu.demo.model.payload.ProductPayload;
import com.jameswu.demo.model.payload.SpecialsPayload;
import com.jameswu.demo.model.response.CommentResponse;
import com.jameswu.demo.repository.CommentRepository;
import com.jameswu.demo.repository.ProductRepository;
import com.jameswu.demo.repository.UserProfileRepository;
import com.jameswu.demo.utils.RedisKey;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductManagementService {

	private final ProductRepository productRepository;
	private final RedisService redisService;
	private final CommentRepository commentRepository;
	private final UserProfileRepository userProfileRepository;

	@Autowired
	public ProductManagementService(
			ProductRepository productRepository,
			RedisService redisService,
			CommentRepository commentRepository,
			UserProfileRepository userProfileRepository) {
		this.productRepository = productRepository;
		this.redisService = redisService;
		this.commentRepository = commentRepository;
		this.userProfileRepository = userProfileRepository;
	}

	@Transactional
	public Product add(ProductPayload payload) {
		return productRepository.save(new Product(
				payload.title(), payload.description(), payload.price(), payload.quantity()));
	}

	public RMap<String, String> addSpecials(SpecialsPayload payload) {
		return redisService.setHashMap(
				RedisKey.withProductPrefix(payload.productId()), payload.specialsDetailPayload());
	}

	@Transactional
	public CommentResponse addComment(GcUser user, CommentPayload payload) {
		productRepository
				.findById(payload.productId())
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		payload.checkIfDefault((id) -> commentRepository
				.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Parent comment not found")));
		var comment = commentRepository.save(
				Comment.to(payload, user.getProfile().getNickname(), user.getUserId()));
		return CommentResponse.from(comment);
	}
}
