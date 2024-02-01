package com.jameswu.security.demo.repository;

import com.jameswu.security.demo.model.GcProfileWithRelation;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GcProfileWithRelationRepository extends CrudRepository<GcProfileWithRelation, UUID> {

    @Query(
            nativeQuery = true,
            value =
                    """
												WITH RECURSIVE RelationHierarchy AS (
														SELECT\s
																descendant_id,\s
																ancestor_id,\s
																1 AS LEVEL\s
														FROM\s
																relation\s
														WHERE\s
																relation.ancestor_id = :ancestor_id\s
														UNION ALL\s
														SELECT\s
																r.descendant_id,\s
																r.ancestor_id,\s
																rh.level + 1\s
														FROM\s
																relation r\s
																INNER JOIN RelationHierarchy rh ON rh.descendant_id = r.ancestor_id\s
														WHERE\s
																rh.level < :level
												)\s
												SELECT\s
														rh.descendant_id,\s
														rh.ancestor_id,\s
														user_profile.address,\s
														user_profile.user_id,\s
														user_profile.email,\s
														user_profile.enrollment_date,\s
														user_profile.recommender_id, \s
														rh.level\s
												FROM\s
														RelationHierarchy rh\s
														INNER JOIN user_profile ON descendant_id = user_profile.user_id;
										""")
    List<GcProfileWithRelation> queryDescendants(@Param("ancestor_id") UUID ancestorId, @Param("level") int level);
}
