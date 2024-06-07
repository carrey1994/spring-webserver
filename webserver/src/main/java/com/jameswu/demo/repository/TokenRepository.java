package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.ActiveToken;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<ActiveToken, Integer> {
	Optional<ActiveToken> findByToken(String token);

	@Query(
			value =
					"""
			SELECT
			t.id,
			t.user_id,
			t.token
			FROM
			active_token t
			INNER JOIN user_profile p ON t.user_id = p.user_id
			WHERE
			p.enrollment_date BETWEEN :prevTime AND :nextTime
			""",
			nativeQuery = true)
	List<ActiveToken> findTokenAfterLastSentTime(
			@Param("prevTime") Instant prevTime, @Param("nextTime") Instant nextTime);
}
