package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {}
