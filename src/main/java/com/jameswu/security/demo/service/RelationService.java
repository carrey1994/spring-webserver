package com.jameswu.security.demo.service;

import com.jameswu.security.demo.model.Relation;
import com.jameswu.security.demo.repository.RelationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationService {

    @Autowired
    private RelationRepository relationRepository;

    public void addRelation(UUID recommenderId, UUID newUserId) {
        List<Relation> relationList = new ArrayList<>(List.of(new Relation(newUserId, newUserId, 0)));
        if (recommenderId != null) {
            relationList.add(new Relation(recommenderId, newUserId, 1));
        }
        relationList.add(new Relation(newUserId, newUserId, 0));
        relationRepository.saveAll(relationList);
    }
}
