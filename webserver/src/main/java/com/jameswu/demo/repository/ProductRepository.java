package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

	//    @EntityGraph(value = "gc_user_graph", type = EntityGraph.EntityGraphType.LOAD)
	Page<Product> findAll(Pageable pageable);
}
