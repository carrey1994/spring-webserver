package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.GcProfileLevel;
import jakarta.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public class GcProfileLevelRepository {
    @Autowired
    private EntityManager entityManager;

    public List<GcProfileLevel> queryChildren(@Param("user_id") int userId, @Param("level") int level) {
        Session session = entityManager.unwrap(Session.class);
        List<Object[]> results = session.createNativeQuery(
                        """
						WITH RECURSIVE userhierarchy AS (
								SELECT
										up.user_id,
										up.email,
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
								user_id, email, address, enrollment_date, recommender_id, level
						FROM
								userhierarchy;
				""")
                .setParameter("user_id", userId)
                .setParameter("level", level)
                .list();
        session.close();

        return results.stream()
                .map((result) -> new GcProfileLevel(
                        (int) result[0],
                        (String) result[1],
                        (String) result[2],
                        ((Timestamp) result[3]).toInstant(),
                        (Integer) result[4],
                        Integer.parseInt(result[5].toString())))
                .toList();
    }
}
