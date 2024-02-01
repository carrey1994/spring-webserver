package com.jameswu.security.demo.service;

import com.jameswu.security.demo.model.GcProfileTreeNode;
import com.jameswu.security.demo.model.GcProfileWithRelation;
import com.jameswu.security.demo.model.UserProfile;
import com.jameswu.security.demo.repository.GcProfileWithRelationRepository;
import com.jameswu.security.demo.repository.RelationRepository;
import com.jameswu.security.demo.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private GcProfileWithRelationRepository gcProfileWithRelationRepository;

    @Autowired
    private UserRepository userRepository;

    public GcProfileTreeNode diagram(UUID userId) {
        return queryByUserIdAndLevel(userId, Integer.MAX_VALUE);
    }

    public GcProfileTreeNode onlyOneGenerationChild(UUID userId) {
        return queryByUserIdAndLevel(userId, 1);
    }

    public GcProfileTreeNode queryByUserIdAndLevel(UUID userId, int level) {
        List<GcProfileWithRelation> gcProfileWithRelations =
                gcProfileWithRelationRepository.queryDescendants(userId, level);
        // todo: get profile from jwt/cache
        UserProfile userProfile = userRepository.findById(userId).get().getUserProfile();
        GcProfileTreeNode gcProfileTreeNode = new GcProfileTreeNode(userProfile, new ArrayList<>());
        makeTreeDiagram(gcProfileTreeNode, gcProfileWithRelations);
        return gcProfileTreeNode;
    }

    public void makeTreeDiagram(
            GcProfileTreeNode gcProfileTreeNode, List<GcProfileWithRelation> gcProfileWithRelations) {
        List<GcProfileWithRelation> children = gcProfileWithRelations.stream()
                .filter(gcProfileWithRelation ->
                        gcProfileTreeNode.getUserProfile().getUserId().equals(gcProfileWithRelation.getAncestorId()))
                .toList();
        List<GcProfileTreeNode> childrenTree = children.stream()
                .map(gcProfileWithRelation -> new GcProfileTreeNode(
                        new UserProfile(
                                gcProfileWithRelation.getUserId(),
                                gcProfileWithRelation.getEmail(),
                                gcProfileWithRelation.getAddress(),
                                gcProfileWithRelation.getEnrollmentDate(),
                                gcProfileWithRelation.getRecommender_id()),
                        new ArrayList<>()))
                .toList();
        gcProfileTreeNode.setDescendantProfiles(childrenTree);
        List<GcProfileWithRelation> subNextChildren = new ArrayList<>(List.copyOf(gcProfileWithRelations));
        subNextChildren.removeAll(children);
        for (GcProfileTreeNode childTreeNode : childrenTree) {
            makeTreeDiagram(childTreeNode, subNextChildren);
        }
    }
}
