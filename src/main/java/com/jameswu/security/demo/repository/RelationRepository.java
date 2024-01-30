package com.jameswu.security.demo.repository;

import com.jameswu.security.demo.model.Relation;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends CrudRepository<Relation, RelationRepository> {

    public List<Relation> findByRelationIdAncestorIdAndDistance(UUID ancestorId, int distance);
}
