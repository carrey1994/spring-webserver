package com.jameswu.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "comment")
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Comment implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int commentId;

	@Column(name = "content", nullable = false)
	@Size(min = 1, max = 100)
	private String content;

	@Column(name = "user_id", updatable = false, insertable = false)
	private int userId;

	@Column(name = "product_id", updatable = false, insertable = false)
	private int productId;

	/**
	 * @param parentCommentId: default value 0 as root
	 */
	@Column(name = "parent_comment_id", updatable = false, insertable = false, nullable = false)
	private int parentCommentId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	@Column(name = "created_time", nullable = false)
	private Instant createdTime;
}
