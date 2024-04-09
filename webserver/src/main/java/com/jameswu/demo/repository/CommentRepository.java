package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {
	Page<Comment> findAllByProductIdOrderByCreatedTime(Pageable pageable, int productId);

	List<Comment> findAllByParentCommentIdOrderByCreatedTime(int parentCommentId);
}
