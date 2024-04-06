package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.payload.SpecialsDetailPayload;
import com.jameswu.demo.model.payload.SpecialsPayload;
import com.jameswu.demo.model.response.CommentResponse;
import com.jameswu.demo.repository.CommentRepository;
import com.jameswu.demo.repository.ProductRepository;
import com.jameswu.demo.utils.RedisKey;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final RedisService redisService;
	private final CacheService cacheService;
	private final CommentRepository commentRepository;

	@Autowired
	public ProductService(
			ProductRepository productRepository,
			RedisService redisService,
			CacheService cacheService,
			CommentRepository commentRepository) {
		this.productRepository = productRepository;
		this.redisService = redisService;
		this.cacheService = cacheService;
		this.commentRepository = commentRepository;
	}

	public List<Product> all(Pageable pageable) {
		return productRepository.findAll(pageable).getContent();
	}

	public List<SpecialsPayload> specials() {
		// todo: cache table
		return List.of();
	}

	public SpecialsPayload specialsById(int productId) {
		var detail = redisService.getHashClass(
				RedisKey.withProductPrefix(productId), SpecialsDetailPayload.class);
		return new SpecialsPayload(productId, detail);
	}

	public List<CommentResponse> comments(Pageable pageable, int productId) {
		return commentRepository
				.findAllByProductIdOrderByCreatedTime(pageable, productId)
				.getContent()
				.stream()
				.map(CommentResponse::from)
				.toList();
	}

	public List<CommentResponse> replies(int parentCommentId) {
		return commentRepository
				.findAllByParentCommentIdOrderByCreatedTime(parentCommentId)
				.stream()
				.map(CommentResponse::from)
				.toList();
	}
}
