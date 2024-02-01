package com.jameswu.security.demo.service;

import com.jameswu.security.demo.model.Relation;
import com.jameswu.security.demo.repository.RelationRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationService {

    @Autowired
    private RelationRepository relationRepository;

    public void addRelation(UUID recommenderId, UUID newUserId) {
        if (recommenderId != null) {
            relationRepository.save(new Relation(recommenderId, newUserId));
        }
    }
}
