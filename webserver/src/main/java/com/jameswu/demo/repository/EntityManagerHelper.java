package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.GcProfileLevel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public class EntityManagerHelper {
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

		Query query = entityManager.createNativeQuery(sql, "GcProfileLevelMapping");
		query.setParameter("user_id", userId);
		query.setParameter("level", level);

		return (List<GcProfileLevel>) query.getResultList();
	}
}
