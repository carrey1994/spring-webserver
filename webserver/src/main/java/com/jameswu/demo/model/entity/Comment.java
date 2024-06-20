package com.jameswu.demo.model.entity;

import com.jameswu.demo.model.payload.CommentPayload;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "comment")
@Table(name = "comment")
@NoArgsConstructor
@Getter
@Setter
public class Comment implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int commentId;

	@Column(name = "content", nullable = false)
	@Size(min = 1, max = 100)
	private String content;

	@Column(name = "user_id", updatable = false, insertable = false)
	private int userId;

	@Column(name = "nickname", nullable = false, updatable = false, insertable = false)
	@NotBlank
	private String nickname;

	@Column(name = "product_id", updatable = false, insertable = false)
	private int productId;

	/**
	 * @param parentCommentId: default value 0 as root
	 */
	@Column(name = "parent_comment_id", updatable = false, insertable = false, nullable = false)
	private int parentCommentId;

	@Column(name = "created_time", nullable = false)
	private Instant createdTime;

	public Comment(
			String content, int userId, String nickname, int productId, int parentCommentId, Instant createdTime) {
		this.content = content;
		this.userId = userId;
		this.nickname = nickname;
		this.productId = productId;
		this.parentCommentId = parentCommentId;
		this.createdTime = createdTime;
	}

	public static Comment to(CommentPayload payload, String nickname, int userId) {
		return new Comment(
				payload.content(), userId, nickname, payload.productId(), payload.parentCommentId(), Instant.now());
	}
}
