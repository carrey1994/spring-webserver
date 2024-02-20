package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.Insurance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepository extends CrudRepository<Insurance, Long> {}
