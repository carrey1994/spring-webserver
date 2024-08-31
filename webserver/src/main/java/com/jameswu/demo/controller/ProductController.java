package com.jameswu.demo.controller;

import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.payload.SpecialsPayload;
import com.jameswu.demo.model.response.CommentResponse;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.service.ProductService;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping("all")
	public Result<List<Product>> all(Pageable pageable) {
		return Result.success(productService.all(pageable));
	}

	@GetMapping("specials/all")
	public Result<List<SpecialsPayload>> specials() {
		return Result.success(productService.specials());
	}

	@GetMapping("specials")
	public Result<SpecialsPayload> specialsById(@PathParam("productId") int productId) {
		return Result.success(productService.specialsById(productId));
	}

	@GetMapping("comments")
	public Result<List<CommentResponse>> comments(Pageable pageable, @RequestParam int productId) {
		return Result.success(productService.comments(pageable, productId));
	}

	@GetMapping("comments/replies")
	public Result<List<CommentResponse>> replies(@RequestParam int parentCommentId) {
		return Result.success(productService.replies(parentCommentId));
	}
}
