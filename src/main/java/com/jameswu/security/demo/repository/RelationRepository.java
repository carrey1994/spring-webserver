package com.jameswu.security.demo.repository;

import com.jameswu.security.demo.model.Relation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends CrudRepository<Relation, RelationRepository> {}
