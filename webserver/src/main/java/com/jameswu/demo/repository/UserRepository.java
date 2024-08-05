package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.enums.UserStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<GcUser, Integer> {

	Optional<GcUser> findByUsername(String username);

	Optional<GcUser> findByUsernameIgnoreCase(String username);

	Optional<GcUser> findByUserId(int userId);

	@EntityGraph(value = "gc_user_graph", type = EntityGraph.EntityGraphType.LOAD)
	Page<GcUser> findByUserStatus(UserStatus status, Pageable pageable);
}
