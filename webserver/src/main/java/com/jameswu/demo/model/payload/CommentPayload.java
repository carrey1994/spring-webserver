package com.jameswu.demo.model.payload;

import jakarta.validation.constraints.Size;
import java.util.function.Consumer;

public record CommentPayload(
		@Size(min = 1, max = 100) String content,
		@Size(min = 1) int productId,
		@Size(min = 0) int parentCommentId) {

	public void checkIfDefault(Consumer<Integer> consumer) {
		if (parentCommentId == 0) {
			consumer.accept(parentCommentId);
		}
	}
}
