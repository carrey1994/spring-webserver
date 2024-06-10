package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.GcProfileLevel;
import com.jameswu.demo.model.entity.UserProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public class GcProfileLevelRepository {
	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<GcProfileLevel> queryChildren(@Param("user_id") int userId, @Param("level") int level) {
		String sql =
				"""
						WITH RECURSIVE userhierarchy AS (
							SELECT
								up.user_id,
								up.email,
								up.nickname,
								up.address,
								up.enrollment_date,
								up.recommender_id,
								1 AS level
							FROM
								user_profile up
							WHERE
								up.user_id = :user_id
							UNION ALL
							SELECT
								up.user_id,
								up.email,
								up.nickname,
								up.address,
								up.enrollment_date,
								up.recommender_id,
								uh.level + 1
							FROM
								user_profile up
								INNER JOIN userhierarchy uh ON up.recommender_id = uh.user_id
							WHERE
								uh.level < :level
						)
						SELECT
							user_id, email, nickname, address, enrollment_date, recommender_id, level
						FROM
							userhierarchy;
				""";

		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("user_id", userId);
		query.setParameter("level", level);

		List<Object[]> resultList = query.getResultList();
		return resultList.stream()
				.map(result -> new GcProfileLevel(
						new UserProfile(
								(int) result[0],
								(String) result[1],
								(String) result[2],
								(String) result[3],
								(Instant) result[4],
								(Integer) result[5]),
						(int) result[6]))
				.collect(Collectors.toList());
	}
}
