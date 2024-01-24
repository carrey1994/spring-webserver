package com.jameswu.security.demo.repository;

import com.jameswu.security.demo.model.GcUser;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<GcUser, UUID> {

    Optional<GcUser> findByUsername(String username);

    Optional<GcUser> findByUserId(UUID userId);

    @Override
    List<GcUser> findAll();
}
