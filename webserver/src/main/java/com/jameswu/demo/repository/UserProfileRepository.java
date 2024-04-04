package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {
    Page<UserProfile> findAll(Pageable pageable);
}
