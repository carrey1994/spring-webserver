package com.jameswu.security.demo.repository;

import com.jameswu.security.demo.model.GcUser;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<GcUser, UUID> {

    public GcUser findByUsername(String username);
}
