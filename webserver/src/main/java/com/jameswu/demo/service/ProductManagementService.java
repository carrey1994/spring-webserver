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
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProductManagementService {

	private final ProductRepository productRepository;
	private final RedisService redisService;
	private final CommentRepository commentRepository;
	private final UserProfileRepository userProfileRepository;

	@Transactional
	public Product add(ProductPayload payload) {
		return productRepository.save(
				new Product(payload.title(), payload.description(), payload.price(), payload.quantity()));
	}

	public Map<String, String> addSpecials(SpecialsPayload payload) {
		return redisService.setHashMap(
				RedisKey.withSpecialsPrefix(payload.productId()), payload.specialsDetailPayload());
	}

	@Transactional
	public CommentResponse addComment(GcUser user, CommentPayload payload) {
		productRepository.findById(payload.productId()).ifPresentOrElse(product -> {}, () -> {
			throw new IllegalArgumentException("Product not found");
		});
		// check if default
		payload.checkIfDefault((id) -> commentRepository.findById(id).ifPresentOrElse(comment -> {}, () -> {
			throw new IllegalArgumentException("Parent comment not found");
		}));
		var comment = Comment.to(payload, user.getProfile().getNickname(), user.getUserId());
		return CommentResponse.from(commentRepository.save(comment));
	}
}
