package com.jameswu.demo.model.response;

import com.jameswu.demo.model.entity.Comment;
import java.io.Serializable;
import java.time.Instant;
import lombok.Data;

@Data
public class CommentResponse implements Serializable {
	private int commentId;
	private String content;
	private String nickname;
	private int productId;
	private int parentCommentId;
	Instant createdTime;

	public CommentResponse(
			int commentId, String content, String nickname, int productId, int parentCommentId, Instant createdTime) {
		this.commentId = commentId;
		this.content = content;
		this.nickname = nickname;
		this.productId = productId;
		this.parentCommentId = parentCommentId;
		this.createdTime = createdTime;
	}

	public static CommentResponse from(Comment comment) {
		return new CommentResponse(
				comment.getCommentId(),
				comment.getContent(),
				comment.getNickname(),
				comment.getProductId(),
				comment.getParentCommentId(),
				comment.getCreatedTime());
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getParentCommentId() {
		return parentCommentId;
	}

	public void setParentCommentId(int parentCommentId) {
		this.parentCommentId = parentCommentId;
	}

	public Instant getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Instant createdTime) {
		this.createdTime = createdTime;
	}
}
