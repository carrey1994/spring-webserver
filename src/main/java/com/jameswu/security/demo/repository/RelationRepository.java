package com.jameswu.security.demo.repository;

import com.jameswu.security.demo.model.Relation;

import java.util.List;
import java.util.UUID;

import com.jameswu.security.demo.model.RelationId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends CrudRepository<Relation, RelationRepository> {

    List<Relation> findByRelationIdAncestorId(UUID ancestorId);

    @Query(
            nativeQuery = true,
            value = """
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
                            INNER JOIN RelationHierarchy rh ON rh.descendant_id = r.ancestor_id
                        )\s
                        SELECT\s
                          descendant_id,\s
                          ancestor_id\s
                        FROM\s
                          RelationHierarchy;
                    """)
    List<Relation> queryDescendants(@Param("ancestor_id") UUID ancestorId);
}
