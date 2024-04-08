package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.ActiveToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<ActiveToken, Integer> {
	Optional<ActiveToken> findByToken(String token);
}
