package com.jameswu.security.demo.service;

import com.jameswu.security.demo.model.GcProfileTreeNode;
import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.GcUserTreeNode;
import com.jameswu.security.demo.model.Relation;
import com.jameswu.security.demo.model.UserProfile;
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
    private UserRepository userRepository;

    public GcProfileTreeNode diagram(UUID userId) {
        GcUserTreeNode root = new GcUserTreeNode(userId, new ArrayList<>());
        UserProfile userProfile =
                userRepository.findByUserId(root.getAncestorId()).get().getUserProfile();
        GcProfileTreeNode gcProfileTreeNode = new GcProfileTreeNode(userProfile, new ArrayList<>());
        treeNode(root);
        treeProfile(root, gcProfileTreeNode);
        return gcProfileTreeNode;
    }

    public GcProfileTreeNode direct(UUID userId) {
        List<Relation> relationList = relationRepository.findByRelationIdAncestorIdAndDistance(userId, 1);
        List<UUID> descendantIds = relationList.stream()
                .map(relation -> relation.getRelationId().getDescendantId())
                .toList();
        UserProfile userProfile = userRepository.findByUserId(userId).get().getUserProfile();
        List<GcProfileTreeNode> descendantProfiles = userRepository.findByUserIdIn(descendantIds).stream()
                .map(user -> new GcProfileTreeNode(user.getUserProfile(), new ArrayList<>()))
                .toList();
        return new GcProfileTreeNode(userProfile, descendantProfiles);
    }

    private void treeNode(GcUserTreeNode node) {
        List<Relation> relationList = relationRepository.findByRelationIdAncestorIdAndDistance(node.getAncestorId(), 1);
        List<GcUserTreeNode> descendantTreeNodes = relationList.stream()
                .map(e -> new GcUserTreeNode(e.getRelationId().getDescendantId(), new ArrayList<>()))
                .toList();
        node.setDescendants(descendantTreeNodes);
        for (GcUserTreeNode child : descendantTreeNodes) {
            treeNode(child);
        }
    }

    private void treeProfile(GcUserTreeNode node, GcProfileTreeNode gcProfileTreeNode) {
        UserProfile userProfile =
                userRepository.findByUserId(node.getAncestorId()).get().getUserProfile();
        gcProfileTreeNode.setUserProfile(userProfile);
        List<UUID> descendantIds = node.getDescendants().stream()
                .map(GcUserTreeNode::getAncestorId)
                .toList();
        List<UserProfile> descendantProfiles = userRepository.findByUserIdIn(descendantIds).stream()
                .map(GcUser::getUserProfile)
                .toList();
        List<GcProfileTreeNode> descendantsProfileTree = descendantProfiles.stream()
                .map(e -> new GcProfileTreeNode(e, new ArrayList<>()))
                .toList();
        gcProfileTreeNode.setDescendantProfiles(descendantsProfileTree);
        for (GcUserTreeNode descendant : node.getDescendants()) {
            UUID descendantId = descendant.getAncestorId();
            GcProfileTreeNode descendantProfile = descendantsProfileTree.stream()
                    .filter(p -> p.getUserProfile().getUserId().equals(descendantId))
                    .findFirst()
                    .get();
            treeProfile(descendant, descendantProfile);
        }
    }
}
